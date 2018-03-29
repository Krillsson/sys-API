package com.krillsson.sysapi.core.windows;

import com.krillsson.sysapi.config.SystemApiConfiguration;
import com.krillsson.sysapi.core.*;
import com.krillsson.sysapi.util.Utils;
import ohmwrapper.MonitorManager;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

public class WindowsInfoProviderFactory extends DefaultInfoProviderFactory {
    private final MonitorManagerFactory monitorManagerFactory;
    private final HardwareAbstractionLayer hal;
    private final OperatingSystem operatingSystem;
    private final SystemApiConfiguration configuration;
    private final SpeedMeasurementManager speedMeasurementManager;
    private final Utils utils;

    private MonitorManager monitorManager;

    public WindowsInfoProviderFactory(MonitorManagerFactory monitorManagerFactory, HardwareAbstractionLayer hal, OperatingSystem operatingSystem, SystemApiConfiguration configuration, SpeedMeasurementManager speedMeasurementManager, Utils utils) {
        super(hal, operatingSystem, configuration, speedMeasurementManager, utils);
        this.monitorManagerFactory = monitorManagerFactory;
        this.hal = hal;
        this.operatingSystem = operatingSystem;
        this.configuration = configuration;
        this.speedMeasurementManager = speedMeasurementManager;
        this.utils = utils;
    }


    @Override
    public boolean prerequisitesFilled() {
        return monitorManagerFactory.prerequisitesFilled();
    }

    @Override
    public boolean initialize() {
        boolean bridgeInitialized = monitorManagerFactory.initialize();
        if (bridgeInitialized) {
            monitorManager = monitorManagerFactory.getMonitorManager();
            setCpuInfoProvider(new WindowsCpuInfoProvider(hal, operatingSystem, monitorManager, utils));
            setNetworkInfoProvider(new WindowsNetworkProvider(hal, speedMeasurementManager, monitorManager));
            setGpuInfoProvider(new WindowsGpuInfoProvider(hal, monitorManager));
            setDiskInfoProvider(new WindowsDiskProvider(operatingSystem, hal, speedMeasurementManager));
            setProcessesInfoProvider(new DefaultProcessesInfoProvider(operatingSystem, hal));
            setMotherboardInfoProvider(new WindowsMotherboardInfoProvider(hal, monitorManager));
            setMemoryInfoProvider(new DefaultMemoryInfoProvider(hal));
        }
        return bridgeInitialized;
    }
}
