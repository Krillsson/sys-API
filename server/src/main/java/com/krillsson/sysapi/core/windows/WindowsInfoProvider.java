package com.krillsson.sysapi.core.windows;

import com.krillsson.sysapi.core.InfoProvider;
import com.krillsson.sysapi.core.InfoProviderBase;
import com.krillsson.sysapi.domain.gpu.Gpu;
import com.krillsson.sysapi.domain.gpu.GpuHealth;
import com.krillsson.sysapi.domain.health.DataType;
import com.krillsson.sysapi.domain.health.HealthData;
import com.krillsson.sysapi.domain.storage.HWDiskHealth;
import com.krillsson.sysapi.domain.storage.HWDiskLoad;
import net.sf.jni4net.Bridge;
import ohmwrapper.*;
import org.slf4j.Logger;
import oshi.json.hardware.HWDiskStore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.krillsson.sysapi.core.windows.util.NullSafeOhmMonitor.nullSafe;
import static com.krillsson.sysapi.util.JarLocation.*;

public class WindowsInfoProvider extends InfoProviderBase implements InfoProvider {

    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WindowsInfoProvider.class);

    private static final File OHM_JNI_WRAPPER_DLL = new File(LIB_LOCATION + SEPARATOR + "OhmJniWrapper.dll");
    private static final File OPEN_HARDWARE_MONITOR_LIB_DLL = new File(LIB_LOCATION + SEPARATOR + "OpenHardwareMonitorLib.dll");
    private static final File OHM_JNI_WRAPPER_J4N_DLL = new File(LIB_LOCATION + SEPARATOR + "OhmJniWrapper.j4n.dll");

    private MonitorManager monitorManager;

    public WindowsInfoProvider() {

    }

    @Override
    public boolean canProvide() {
        return OHM_JNI_WRAPPER_DLL.exists() &&
                OPEN_HARDWARE_MONITOR_LIB_DLL.exists() &&
                OHM_JNI_WRAPPER_DLL.exists() &&
                initBridge();
    }

    public GpuMonitor[] ohmGpu() {
        monitorManager.Update();
        return monitorManager.GpuMonitors();
    }

/*
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
    public System systemSummary(int filesystemId, String nicId) {
        System system = super.systemSummary(filesystemId, nicId);
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
    public Drive getFileSystemById(int name) {
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
        NetworkInterfaceConfig config = super.getConfigById(id);
        setSpeed(config);
        return config;
    }

    private void setSpeed(NetworkInterfaceConfig config) {
        NetworkMonitor networkMonitor = monitorManager.getNetworkMonitor();
        NicInfo[] nics = networkMonitor.getNics();
        for (NicInfo info : nics) {
            if (info.getPhysicalAddress().equals(config.getHwaddr())) {
                config.setNetworkInterfaceSpeed(new NetworkInterfaceSpeed((long) (info.getInBandwidth().getValue() * 1000), (long) (info.getOutBandwidth().getValue() * 1000)));
            }
        }
    }

    @Override


    private void matchDriveProperties(List<Drive> drives, DriveMonitor[] driveMonitors) {
        if (driveMonitors != null && driveMonitors.length > 0) {
            for (DriveMonitor driveMonitor : driveMonitors) {
                for (Drive drive : drives) {
                    if (driveNamesAreEqual(driveMonitor, drive)) {
                        drive.setHealth(new HWDiskHealth(nullSafe(driveMonitor.getTemperature()).getValue(),
                                nullSafe(driveMonitor.getRemainingLife()).getValue(),
                                Arrays.asList(nullSafe(driveMonitor.getHealthData()))
                                        .stream()
                                        .map(l -> new HealthData(l.getLabel(), l.getValue()))
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
    }*/

    public Gpu[] gpus() {
        List<Gpu> gpus = new ArrayList<>();
        monitorManager.Update();
        final GpuMonitor[] gpuMonitors = monitorManager.GpuMonitors();
        if (gpuMonitors != null && gpuMonitors.length > 0) {
            for (GpuMonitor gpuMonitor : gpuMonitors) {
                GpuHealth gpuHealth = new GpuHealth(
                        nullSafe(gpuMonitor.getFanRPM()).getValue(), nullSafe(gpuMonitor.getFanPercent()).getValue(), nullSafe(gpuMonitor.getTemperature()).getValue(),
                        nullSafe(gpuMonitor.getCoreLoad()).getValue(),
                        nullSafe(gpuMonitor.getMemoryClock()).getValue());
                Gpu gpu = new Gpu(
                        gpuMonitor.getVendor(),
                        gpuMonitor.getName(),
                        nullSafe(gpuMonitor.getCoreClock()).getValue(),
                        nullSafe(gpuMonitor.getMemoryClock()).getValue(),
                        gpuHealth
                );
                gpus.add(gpu);
            }
        }
        return gpus.toArray(/*type reference*/new Gpu[0]);
    }

    public GpuHealth[] gpuHealths() {
        List<GpuHealth> gpus = new ArrayList<>();
        monitorManager.Update();
        final GpuMonitor[] gpuMonitors = monitorManager.GpuMonitors();
        if (gpuMonitors != null && gpuMonitors.length > 0) {
            for (GpuMonitor gpuMonitor : gpuMonitors) {
                GpuHealth gpuHealth = new GpuHealth(
                        nullSafe(gpuMonitor.getFanRPM()).getValue(), nullSafe(gpuMonitor.getFanPercent()).getValue(), nullSafe(gpuMonitor.getTemperature()).getValue(),
                        nullSafe(gpuMonitor.getCoreLoad()).getValue(),
                        nullSafe(gpuMonitor.getMemoryClock()).getValue());
                gpus.add(gpuHealth);
            }
        }
        return gpus.toArray(/*type reference*/new GpuHealth[0]);
    }

    private boolean initBridge() {
        LOGGER.info("Enabling OHMJNIWrapper impl. Disable this in the configuration.yml (see README.md)");
        Bridge.setDebug(true);
        try {
            Bridge.init();
        } catch (IOException e) {
            LOGGER.error("Trouble while initializing JNI4Net Bridge. Do I have admin privileges?", e);
            return false;
        }

        OHMManagerFactory factory = loadFromInstallDir();
        try {
            factory.init();
            this.monitorManager = factory.GetManager();
            return true;
        } catch (Exception e) {
            LOGGER.error("Trouble while initializing JNI4Net Bridge. Do I have admin privileges?", e);
            return false;
        }
    }

    private OHMManagerFactory loadFromInstallDir() {
        try {
            Bridge.LoadAndRegisterAssemblyFrom(OHM_JNI_WRAPPER_DLL);
            Bridge.LoadAndRegisterAssemblyFrom(OHM_JNI_WRAPPER_J4N_DLL);
            Bridge.LoadAndRegisterAssemblyFrom(OPEN_HARDWARE_MONITOR_LIB_DLL);
            return new OHMManagerFactory();
        } catch (Exception e) {
            LOGGER.error("Unable to load OHM from installation directory {}", SOURCE_LIB_LOCATION, e);
            return null;
        }

    }

    @Override
    public HWDiskHealth diskHealth(String name, HWDiskStore diskStore) {
        monitorManager.Update();
        DriveMonitor[] driveMonitors = monitorManager.DriveMonitors();
        for (DriveMonitor driveMonitor : driveMonitors) {
            if (driveMonitor.getLogicalName().equals(name)) {
                List<HealthData> healthData = new ArrayList<>();
                addIfSafe(healthData, driveMonitor.getRemainingLife());
                if (driveMonitor.getLifecycleData() != null) {
                    for (OHMSensor sensor : driveMonitor.getLifecycleData()) {
                        addIfSafe(healthData, sensor);
                    }
                }

                return new HWDiskHealth(nullSafe(driveMonitor.getTemperature()).getValue(),
                        new HWDiskLoad(driveMonitor.getReadRate(),
                                driveMonitor.getWriteRate()),
                        healthData);
            }
        }
        return null;
    }

    @Override
    public double[] cpuTemperatures() {
        double[] temperatures = new double[0];
        monitorManager.Update();
        if (monitorManager.CpuMonitors().length > 0) {
            CpuMonitor cpuMonitor = monitorManager.CpuMonitors()[0];
            temperatures = new double[]{nullSafe(cpuMonitor.getPackageTemperature()).getValue()};
            if (nullSafe(cpuMonitor.getTemperatures()).length >= 1) {
                final OHMSensor[] sensors = cpuMonitor.getTemperatures();
                temperatures = new double[sensors.length];
                for (int i = 0; i < sensors.length; i++) {
                    OHMSensor sensor = sensors[i];
                    temperatures[i] = sensor.getValue();
                }
            }
        }
        return temperatures;
    }

    @Override
    public double cpuFanRpm() {
        monitorManager.Update();
        if (monitorManager.CpuMonitors().length > 0) {
            CpuMonitor cpuMonitor = monitorManager.CpuMonitors()[0];
            return nullSafe(cpuMonitor.getFanRPM()).getValue();
        }
        return 0;
    }

    @Override
    public double cpuFanPercent() {
        monitorManager.Update();
        if (monitorManager.CpuMonitors().length > 0) {
            CpuMonitor cpuMonitor = monitorManager.CpuMonitors()[0];
            return nullSafe(cpuMonitor.getFanPercent()).getValue();
        }
        return 0;
    }

    @Override
    public List<HealthData> healthData() {
        List<HealthData> list = new ArrayList<>();
        monitorManager.Update();
        MainboardMonitor mainboardMonitor = monitorManager.getMainboardMonitor();
        if (mainboardMonitor != null) {
            addIfSafe(list, nullSafe(mainboardMonitor.getBoardFanPercent()));
            addIfSafe(list, nullSafe(mainboardMonitor.getBoardFanRPM()));
            addIfSafe(list, nullSafe(mainboardMonitor.getBoardTemperatures()));
        }
        return list;
    }

    private void addIfSafe(List<HealthData> healthData, OHMSensor sensor) {
        OHMSensor ohmSensor = nullSafe(sensor);
        if (ohmSensor.getValue() > 0) {
            com.krillsson.sysapi.domain.health.DataType dataType = DataType.valueOf(sensor.getDataType().toString().toUpperCase());
            healthData.add(new HealthData(ohmSensor.getLabel(), ohmSensor.getValue(), dataType));
        }
    }

    private void addIfSafe(List<HealthData> healthDataList, OHMSensor[] sensors) {
        for (OHMSensor sensor : sensors) {
            addIfSafe(healthDataList, sensor);
        }
    }
}
