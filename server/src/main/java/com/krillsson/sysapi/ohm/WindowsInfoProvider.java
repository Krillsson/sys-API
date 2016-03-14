package com.krillsson.sysapi.ohm;

import com.krillsson.sysapi.domain.cpu.Cpu;
import com.krillsson.sysapi.domain.cpu.CpuLoad;
import com.krillsson.sysapi.domain.drive.*;
import com.krillsson.sysapi.domain.gpu.Gpu;
import com.krillsson.sysapi.domain.gpu.GpuInfo;
import com.krillsson.sysapi.domain.gpu.GpuLoad;
import com.krillsson.sysapi.domain.motherboard.Motherboard;
import com.krillsson.sysapi.domain.network.NetworkInfo;
import com.krillsson.sysapi.domain.network.NetworkInterfaceConfig;
import com.krillsson.sysapi.domain.network.NetworkInterfaceSpeed;
import com.krillsson.sysapi.domain.system.System;
import com.krillsson.sysapi.provider.DefaultInfoProvider;
import net.sf.jni4net.Bridge;
import ohmwrapper.*;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.krillsson.sysapi.util.NullSafeOhmMonitor.nullSafe;
import static java.util.Collections.singletonList;

public class WindowsInfoProvider extends DefaultInfoProvider {
    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WindowsInfoProvider.class.getSimpleName());

    MonitorManager monitorManager;

    public WindowsInfoProvider() {
        initBridge();
    }

    @Override
    public Cpu cpu() {
        Cpu cpu = super.cpu();
        cpu.setStatistics(statistics());
        monitorManager.Update();
        if (monitorManager.CpuMonitors().length > 0) {
            CpuMonitor cpuMonitor = monitorManager.CpuMonitors()[0];
            cpu.setFanPercent(nullSafe(cpuMonitor.getFanPercent()).getValue());
            cpu.setFanRpm(nullSafe(cpuMonitor.getFanRPM()).getValue());
            cpu.setTemperature(nullSafe(cpuMonitor.getPackageTemperature()).getValue());
            cpu.setVoltage(nullSafe(cpuMonitor.getVoltage()).getValue());
            cpu.getTotalCpuLoad().setTemperature(nullSafe(cpuMonitor.getPackageTemperature()).getValue());
            if (nullSafe(cpuMonitor.getTemperatures()).length >= 1 &&
                    cpuMonitor.getTemperatures().length == cpu.getCpuLoadPerCore().size()) {
                final List<CpuLoad> cpuLoadPerCore = cpu.getCpuLoadPerCore();
                final OHMSensor[] temperatures = cpuMonitor.getTemperatures();
                for (int i = 0; i < cpuLoadPerCore.size(); i++) {
                    cpuLoadPerCore.get(i).setTemperature(temperatures[i].getValue());
                }
            }
        }
        return cpu;
    }

    @Override
    public System systemSummary(String filesystemId, String nicId) {
        System system =  super.systemSummary(filesystemId, nicId);
        monitorManager.Update();
        DriveMonitor[] driveMonitors = monitorManager.DriveMonitors();
        if (monitorManager.CpuMonitors().length > 0) {
            CpuMonitor cpuMonitor = monitorManager.CpuMonitors()[0];
            system.getTotalCpuLoad().setTemperature(nullSafe(cpuMonitor.getPackageTemperature()).getValue());
        }
        matchDriveProperties(singletonList(system.getMainFileSystem()), driveMonitors);
        setSpeed(system.getMainNetworkInterface());
        return system;
    }

    @Override
    public CpuLoad getCpuTimeByCoreIndex(int id) {
        CpuLoad cpuLoad = super.getCpuTimeByCoreIndex(id);
        monitorManager.Update();
        if (monitorManager.CpuMonitors().length > 0) {
            CpuMonitor cpuMonitor = monitorManager.CpuMonitors()[0];
            if (nullSafe(cpuMonitor.getTemperatures()).length >= 1 &&
                    cpuMonitor.getTemperatures().length - 1 <= id) {
                final OHMSensor[] temperatures = cpuMonitor.getTemperatures();
                cpuLoad.setTemperature(temperatures[id].getValue());
            }
        }
        return cpuLoad;
    }

    @Override
    public List<Drive> drives() {
        List<Drive> drives = super.drives();
        monitorManager.Update();
        DriveMonitor[] driveMonitors = monitorManager.DriveMonitors();
        matchDriveProperties(drives, driveMonitors);
        return drives;
    }

    @Override
    public List<Drive> getFileSystemsWithCategory(FileSystemType fsType) {
        List<Drive> drives = super.getFileSystemsWithCategory(fsType);
        monitorManager.Update();
        DriveMonitor[] driveMonitors = monitorManager.DriveMonitors();
        matchDriveProperties(drives, driveMonitors);
        return drives;
    }

    @Override
    public Drive getFileSystemById(String name) {
        Drive fileSystemById = super.getFileSystemById(name);
        monitorManager.Update();
        DriveMonitor[] driveMonitors = monitorManager.DriveMonitors();
        matchDriveProperties(singletonList(fileSystemById), driveMonitors);
        return fileSystemById;
    }

    @Override
    public List<Gpu> gpus() {
        List<Gpu> gpus = new ArrayList<>();
        monitorManager.Update();
        final GpuMonitor[] gpuMonitors = monitorManager.GpuMonitors();
        if (gpuMonitors != null && gpuMonitors.length > 0) {
            for (GpuMonitor gpuMonitor : gpuMonitors) {
                GpuLoad gpuLoad = new GpuLoad(
                        nullSafe(gpuMonitor.getTemperature()).getValue(),
                        nullSafe(gpuMonitor.getCoreLoad()).getValue(),
                        nullSafe(gpuMonitor.getMemoryClock()).getValue());
                GpuInfo gpuInfo = new GpuInfo(gpuMonitor.getVendor(),
                        gpuMonitor.getName(),
                        nullSafe(gpuMonitor.getCoreClock()).getValue(),
                        nullSafe(gpuMonitor.getMemoryClock()).getValue()
                );
                Gpu gpu = new Gpu(nullSafe(gpuMonitor.getFanRPM()).getValue(),
                        nullSafe(gpuMonitor.getFanPercent()).getValue(),
                        gpuInfo,
                        gpuLoad
                );
                gpus.add(gpu);
            }
        }
        return gpus;
    }

    @Override
    public NetworkInfo networkInfo() {
        monitorManager.Update();
        NetworkInfo info = super.networkInfo();
        for (NetworkInterfaceConfig conf : info.getNetworkInterfaceConfigs()) {
            setSpeed(conf);
        }
        return info;
    }

    @Override
    public NetworkInterfaceConfig getConfigById(String id) {
        monitorManager.Update();
        NetworkInterfaceConfig config =  super.getConfigById(id);
        setSpeed(config);
        return config;
    }

    private void setSpeed(NetworkInterfaceConfig config) {
        NetworkMonitor networkMonitor = monitorManager.getNetworkMonitor();
        NicInfo[] nics = networkMonitor.getNics();
        for (NicInfo info : nics) {
            if (info.getPhysicalAddress().equals(config.getHwaddr())) {
                config.setNetworkInterfaceSpeed(new NetworkInterfaceSpeed((long)(info.getInBandwidth().getValue() * 1000), (long)(info.getOutBandwidth().getValue() * 1000)));
            }
        }
    }

    @Override
    public Motherboard motherboard() {
        monitorManager.Update();
        MainboardMonitor mainboardMonitor = monitorManager.getMainboardMonitor();
        if (mainboardMonitor != null) {
            Map<String, Double> boardTemperatures = Arrays.asList(nullSafe(mainboardMonitor.getBoardTemperatures())).stream().collect(Collectors.toMap(
                    OHMSensor::getLabel,
                    OHMSensor::getValue));
            Map<String, Double> boardFanRpms = Arrays.asList(nullSafe(mainboardMonitor.getBoardFanRPM())).stream().collect(Collectors.toMap(
                    OHMSensor::getLabel,
                    OHMSensor::getValue));
            Map<String, Double> boardFanPercents = Arrays.asList(nullSafe(mainboardMonitor.getBoardFanPercent())).stream().collect(Collectors.toMap(
                    OHMSensor::getLabel,
                    OHMSensor::getValue));
            return new Motherboard(mainboardMonitor.getName(), boardTemperatures, boardFanRpms, boardFanPercents);
        }
        return null;
    }

    private void matchDriveProperties(List<Drive> drives, DriveMonitor[] driveMonitors) {
        if (driveMonitors != null && driveMonitors.length > 0) {
            for (DriveMonitor driveMonitor : driveMonitors) {
                for (Drive drive : drives) {
                    if (driveNamesAreEqual(driveMonitor, drive)) {
                        drive.setHealth(new DriveHealth(nullSafe(driveMonitor.getTemperature()).getValue(),
                                nullSafe(driveMonitor.getRemainingLife()).getValue(),
                                Arrays.asList(nullSafe(driveMonitor.getLifecycleData()))
                                        .stream()
                                        .map(l -> new LifecycleData(l.getLabel(), l.getValue()))
                                        .collect(Collectors.toList())));
                        drive.setLoad(new DriveLoad(driveMonitor.getReadRate(), driveMonitor.getWriteRate()));
                        drive.setDeviceName(driveMonitor.getName());
                    }
                }
            }
        }
    }

    private boolean driveNamesAreEqual(DriveMonitor driveMonitor, Drive drive) {
        if (driveMonitor.getLogicalName() != null) {
            String driveMonitorName = driveMonitor
                    .getLogicalName()
                    .toLowerCase()
                    .replace(":", "")
                    .replace("\\", "");
            String driveName = drive
                    .deviceName()
                    .toLowerCase()
                    .replace(":", "")
                    .replace("\\", "");
            return driveMonitorName.equals(driveName);
        } else {
            return false;
        }
    }

    private void initBridge() {
        Bridge.setVerbose(true);
        try {
            Bridge.init();
        } catch (IOException e) {
            LOGGER.error("Trouble while initializing JNI4Net Bridge. Do I have admin privileges?");
            throw new RuntimeException("Unable to initialize JNI4Net Bridge.", e);
        }
        File ohmJniWrapperDll;
        File ohmJniWrapperJ4nDll;
        File openHardwareMonitorLibDll;
        if (new File("server/lib/OhmJniWrapper.dll").exists()) {
            //For testing
            ohmJniWrapperDll = new File("server/lib/OhmJniWrapper.dll");
            ohmJniWrapperJ4nDll = new File("server/lib/OhmJniWrapper.j4n.dll");
            openHardwareMonitorLibDll = new File("server/lib/OpenHardwareMonitorLib.dll");
        } else {
            //For deployment
            ohmJniWrapperDll = new File("lib/OhmJniWrapper.dll");
            ohmJniWrapperJ4nDll = new File("lib/OhmJniWrapper.j4n.dll");
            openHardwareMonitorLibDll = new File("lib/OpenHardwareMonitorLib.dll");
        }

        Bridge.LoadAndRegisterAssemblyFrom(ohmJniWrapperDll);
        Bridge.LoadAndRegisterAssemblyFrom(ohmJniWrapperJ4nDll);
        Bridge.LoadAndRegisterAssemblyFrom(openHardwareMonitorLibDll);
        try {
            OHMManagerFactory factory = new OHMManagerFactory();
            factory.init();
            this.monitorManager = factory.GetManager();
        } catch (Exception e) {
            throw new RuntimeException("Unable to initialize JNI4Net Bridge. Do I have admin privileges? Crashing now", e);
        }
    }
}
