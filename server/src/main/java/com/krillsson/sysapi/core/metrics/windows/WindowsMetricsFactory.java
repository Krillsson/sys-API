package com.krillsson.sysapi.core.metrics.windows;

import com.krillsson.sysapi.config.SystemApiConfiguration;
import com.krillsson.sysapi.core.*;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMetricsFactory;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMemoryMetrics;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultProcessesMetrics;
import com.krillsson.sysapi.util.Utils;
import ohmwrapper.MonitorManager;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

public class WindowsMetricsFactory extends DefaultMetricsFactory {
    private final MonitorManagerFactory monitorManagerFactory;
    private final HardwareAbstractionLayer hal;
    private final OperatingSystem operatingSystem;
    private final SpeedMeasurementManager speedMeasurementManager;
    private final Utils utils;

    private MonitorManager monitorManager;

    public WindowsMetricsFactory(MonitorManagerFactory monitorManagerFactory, HardwareAbstractionLayer hal, OperatingSystem operatingSystem, SpeedMeasurementManager speedMeasurementManager, Utils utils) {
        super(hal, operatingSystem, speedMeasurementManager, utils);
        this.monitorManagerFactory = monitorManagerFactory;
        this.hal = hal;
        this.operatingSystem = operatingSystem;
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
            setCpuMetrics(new WindowsCpuMetrics(hal, operatingSystem, monitorManager, utils));
            setNetworkMetrics(new WindowsNetworkMetrics(hal, speedMeasurementManager, monitorManager));
            setGpuMetrics(new WindowsGpuMetrics(hal, monitorManager));
            setDiskMetrics(new WindowsDiskProvider(operatingSystem, hal, speedMeasurementManager));
            setProcessesMetrics(new DefaultProcessesMetrics(operatingSystem, hal));
            setMotherboardMetrics(new WindowsMotherboardMetrics(hal, monitorManager));
            setMemoryMetrics(new DefaultMemoryMetrics(hal));
        }
        return bridgeInitialized;
    }
}
