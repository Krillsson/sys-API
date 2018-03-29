/*
 * Sys-Api (https://github.com/Krillsson/sys-api)
 *
 * Copyright 2017 Christian Jensen / Krillsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Maintainers:
 * contact[at]christian-jensen[dot]se
 */
package com.krillsson.sysapi.core.windows;

import com.krillsson.sysapi.core.DefaultDiskProvider;
import com.krillsson.sysapi.core.DefaultInfoProvider;
import com.krillsson.sysapi.core.DefaultNetworkProvider;
import com.krillsson.sysapi.core.domain.gpu.Gpu;
import com.krillsson.sysapi.core.domain.gpu.GpuHealth;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed;
import com.krillsson.sysapi.core.domain.sensors.DataType;
import com.krillsson.sysapi.core.domain.sensors.HealthData;
import com.krillsson.sysapi.core.domain.storage.DiskHealth;
import com.krillsson.sysapi.util.Utils;
import net.sf.jni4net.Bridge;
import ohmwrapper.*;
import org.slf4j.Logger;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.krillsson.sysapi.core.windows.util.NullSafeOhmMonitor.nullSafe;
import static com.krillsson.sysapi.util.JarLocation.*;

public class WindowsInfoProvider extends DefaultInfoProvider  {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WindowsInfoProvider.class);

    private static final File OHM_JNI_WRAPPER_DLL = new File(LIB_LOCATION + SEPARATOR + "OhmJniWrapper.dll");
    private static final File OPEN_HARDWARE_MONITOR_LIB_DLL = new File(LIB_LOCATION + SEPARATOR + "OpenHardwareMonitorLib.dll");
    private static final File OHM_JNI_WRAPPER_J4N_DLL = new File(LIB_LOCATION + SEPARATOR + "OhmJniWrapper.j4n.dll");
    private final HardwareAbstractionLayer hal;
    private WindowsNetworkProvider windowsNetworkProvider;
    private final WindowsDiskProvider windowsDiskProvider;
    private MonitorManager monitorManager;

    public WindowsInfoProvider(HardwareAbstractionLayer hal, OperatingSystem operatingSystem, Utils utils, WindowsNetworkProvider windowsNetworkProvider, WindowsDiskProvider windowsDiskProvider)
    {
        super(hal, operatingSystem, utils, windowsNetworkProvider, windowsDiskProvider);
        this.hal = hal;
        this.windowsNetworkProvider = windowsNetworkProvider;
        this.windowsDiskProvider = windowsDiskProvider;
    }

    @Override
    public boolean canProvide() {
        return OHM_JNI_WRAPPER_DLL.exists() &&
                OPEN_HARDWARE_MONITOR_LIB_DLL.exists() &&
                OHM_JNI_WRAPPER_DLL.exists() &&
                initBridge();
    }


    private boolean initBridge() {
        LOGGER.info("Enabling OHMJNIWrapper impl. Disable this in the configuration.yml (see README.md)");
        Bridge.setDebug(true);
        try {
            Bridge.init();
        } catch (IOException | UnsupportedOperationException e) {
            LOGGER.error("Trouble while initializing JNI4Net Bridge. Do I have admin privileges?", e);
            return false;
        }

        OHMManagerFactory factory = loadFromInstallDir();
        if(factory != null) {
            try {
                factory.init();
                this.monitorManager = factory.GetManager();
                this.windowsDiskProvider.setMonitorManager(monitorManager);
                this.windowsNetworkProvider.setMonitorManager(monitorManager);
                return true;
            } catch (Exception e) {
                LOGGER.error("Trouble while initializing JNI4Net Bridge. Do I have admin privileges?", e);
                return false;
            }
        }
        return false;
    }

    private OHMManagerFactory loadFromInstallDir() {
        try {
            LOGGER.debug("Attempting to load {}", OHM_JNI_WRAPPER_DLL);
            Bridge.LoadAndRegisterAssemblyFrom(OHM_JNI_WRAPPER_DLL);
            LOGGER.debug("Attempting to load {}", OHM_JNI_WRAPPER_J4N_DLL);
            Bridge.LoadAndRegisterAssemblyFrom(OHM_JNI_WRAPPER_J4N_DLL);
            LOGGER.debug("Attempting to load {}", OPEN_HARDWARE_MONITOR_LIB_DLL);
            Bridge.LoadAndRegisterAssemblyFrom(OPEN_HARDWARE_MONITOR_LIB_DLL);
            return new OHMManagerFactory();
        } catch (Exception e) {
            LOGGER.error("Unable to load OHM from installation directory {}", SOURCE_LIB_LOCATION, e);
            return null;
        }

    }

    @Override
    public DiskHealth diskHealth(String name) {
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

                return new DiskHealth(nullSafe(driveMonitor.getTemperature()).getValue(), healthData.toArray(/* type reference*/new HealthData[0]));
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
    public HealthData[] mainboardHealthData() {
        List<HealthData> list = new ArrayList<>();
        monitorManager.Update();
        MainboardMonitor mainboardMonitor = monitorManager.getMainboardMonitor();
        if (mainboardMonitor != null) {
            addIfSafe(list, nullSafe(mainboardMonitor.getBoardFanPercent()));
            addIfSafe(list, nullSafe(mainboardMonitor.getBoardFanRPM()));
            addIfSafe(list, nullSafe(mainboardMonitor.getBoardTemperatures()));
        }
        return list.toArray(/*type reference*/new HealthData[0]);
    }

    private void addIfSafe(List<HealthData> healthData, OHMSensor sensor) {
        OHMSensor ohmSensor = nullSafe(sensor);
        if (ohmSensor.getValue() > 0) {
            com.krillsson.sysapi.core.domain.sensors.DataType dataType = DataType.valueOf(sensor.getDataType().toString().toUpperCase());
            healthData.add(new HealthData(ohmSensor.getLabel(), ohmSensor.getValue(), dataType));
        }
    }

    private void addIfSafe(List<HealthData> healthDataList, OHMSensor[] sensors) {
        for (OHMSensor sensor : sensors) {
            addIfSafe(healthDataList, sensor);
        }
    }
}
