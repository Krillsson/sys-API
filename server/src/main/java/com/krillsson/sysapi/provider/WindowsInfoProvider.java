package com.krillsson.sysapi.provider;

import com.krillsson.sysapi.domain.cpu.Cpu;
import com.krillsson.sysapi.domain.cpu.CpuLoad;
import com.krillsson.sysapi.domain.filesystem.Drive;
import com.krillsson.sysapi.domain.filesystem.DriveHealth;
import com.krillsson.sysapi.domain.filesystem.FileSystemType;
import net.sf.jni4net.Bridge;
import ohmwrapper.*;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.krillsson.sysapi.util.NullSafeOhmMonitor.*;
import static java.util.Collections.singletonList;

public class WindowsInfoProvider extends DefaultInfoProvider
{
    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WindowsInfoProvider.class.getSimpleName());

    MonitorManager monitorManager;

    protected WindowsInfoProvider()
    {
        try
        {
            initBridge();
        }
        catch (Exception e)
        {
            LOGGER.error("Unable to initialize JNI4Net Bridge. Do I have admin privileges?", e);
        }
    }

    @Override
    public Cpu cpu()
    {
        Cpu cpu = super.cpu();
        monitorManager.Update();
        if (monitorManager.CpuMonitors().length > 0)
        {
            CpuMonitor cpuMonitor = monitorManager.CpuMonitors()[0];
            cpu.setFanPercent(nullSafe(cpuMonitor.getFanPercent()).getValue());
            cpu.setFanRpm(nullSafe(cpuMonitor.getFanRPM()).getValue());
            cpu.setTemperature(nullSafe(cpuMonitor.getPackageTemperature()).getValue());
            cpu.setVoltage(nullSafe(cpuMonitor.getVoltage()).getValue());
            if (nullSafe(cpuMonitor.getTemperatures()).length >= 1 &&
                    cpuMonitor.getTemperatures().length == cpu.getCpuLoadPerCore().size())
            {
                final List<CpuLoad> cpuLoadPerCore = cpu.getCpuLoadPerCore();
                final OHMSensor[] temperatures = cpuMonitor.getTemperatures();
                for (int i = 0; i < cpuLoadPerCore.size(); i++)
                {
                    cpuLoadPerCore.get(i).setTemperature(temperatures[i].getValue());
                }
            }
        }
        return cpu;
    }

    @Override
    public CpuLoad getCpuTimeByCoreIndex(int id)
    {
        CpuLoad cpuLoad = super.getCpuTimeByCoreIndex(id);
        monitorManager.Update();
        if (monitorManager.CpuMonitors().length > 0)
        {
            CpuMonitor cpuMonitor = monitorManager.CpuMonitors()[0];
            if (nullSafe(cpuMonitor.getTemperatures()).length >= 1 &&
                    cpuMonitor.getTemperatures().length - 1 <= id)
            {
                final OHMSensor[] temperatures = cpuMonitor.getTemperatures();
                cpuLoad.setTemperature(temperatures[id].getValue());
            }
        }
        return cpuLoad;
    }

    @Override
    public List<Drive> drives()
    {
        List<Drive> drives = super.drives();
        monitorManager.Update();
        DriveMonitor[] driveMonitors = monitorManager.DriveMonitors();
        matchDriveProperties(drives, driveMonitors);
        return drives;
    }

    @Override
    public List<Drive> getFileSystemsWithCategory(FileSystemType fsType)
    {
        List<Drive> drives = super.getFileSystemsWithCategory(fsType);
        monitorManager.Update();
        DriveMonitor[] driveMonitors = monitorManager.DriveMonitors();
        matchDriveProperties(drives, driveMonitors);
        return drives;
    }

    @Override
    public Drive getFileSystemById(String name)
    {
        Drive fileSystemById = super.getFileSystemById(name);
        monitorManager.Update();
        DriveMonitor[] driveMonitors = monitorManager.DriveMonitors();
        matchDriveProperties(singletonList(fileSystemById), driveMonitors);
        return fileSystemById;
    }

    private void matchDriveProperties(List<Drive> drives, DriveMonitor[] driveMonitors)
    {
        if (driveMonitors != null && driveMonitors.length > 0)
        {
            for (DriveMonitor driveMonitor : driveMonitors)
            {
                for (Drive drive : drives)
                {
                    if (driveMonitor.getLogicalName() != null)
                    {
                        String driveMonitorName = driveMonitor.getLogicalName().toLowerCase().replace(":", "").replace(
                                "\\",
                                "");
                        String driveName = drive.deviceName().toLowerCase().replace(":", "").replace("\\", "");
                        LOGGER.info("Drivename: {} sigarName: {}", driveMonitorName, driveName);
                        if (driveMonitorName.equals(driveName))
                        {
                            drive.setHealth(new DriveHealth(nullSafe(driveMonitor.getTemperature()).getValue(),
                                    nullSafe(driveMonitor.getRemainingLife()).getValue(),
                                    Arrays.asList(nullSafe(driveMonitor.getLifecycleData())).stream().collect(Collectors.toMap(
                                            OHMSensor::getLabel,
                                            OHMSensor::getValue)))
                            );
                        }
                    }
                }
            }
        }
    }

    private void initBridge() throws IOException
    {
        Bridge.setVerbose(true);
        Bridge.init();
        File ohmJniWrapperDll;
        File ohmJniWrapperJ4nDll;
        File openHardwareMonitorLibDll;
        if (new File("server/lib/OhmJniWrapper.dll").exists())
        {
            //For testing
            ohmJniWrapperDll = new File("server/lib/OhmJniWrapper.dll");
            ohmJniWrapperJ4nDll = new File("server/lib/OhmJniWrapper.j4n.dll");
            openHardwareMonitorLibDll = new File("server/lib/OpenHardwareMonitorLib.dll");
        }
        else
        {
            //For deployment
            ohmJniWrapperDll = new File("lib/OhmJniWrapper.dll");
            ohmJniWrapperJ4nDll = new File("lib/OhmJniWrapper.j4n.dll");
            openHardwareMonitorLibDll = new File("lib/OpenHardwareMonitorLib.dll");
        }

        Bridge.LoadAndRegisterAssemblyFrom(ohmJniWrapperDll);
        Bridge.LoadAndRegisterAssemblyFrom(ohmJniWrapperJ4nDll);
        Bridge.LoadAndRegisterAssemblyFrom(openHardwareMonitorLibDll);
        OHMManagerFactory factory = new OHMManagerFactory();
        factory.init();
        this.monitorManager = factory.GetManager();
    }
}
