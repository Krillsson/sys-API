package com.krillsson.sysapi.core.metrics.windows;

import com.krillsson.sysapi.core.speed.SpeedMeasurementManager;
import com.krillsson.sysapi.util.Ticker;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMemoryMetrics;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMetrics;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultProcessesMetrics;
import com.krillsson.sysapi.util.Utils;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

public class WindowsMetrics extends DefaultMetrics {
    private final MonitorManagerFactory monitorManagerFactory;
    private final HardwareAbstractionLayer hal;
    private final OperatingSystem operatingSystem;
    private final SpeedMeasurementManager speedMeasurementManager;
    private final Utils utils;
    private final Ticker ticker;

    private DelegatingMonitorManager monitorManager;

    public WindowsMetrics(MonitorManagerFactory monitorManagerFactory, HardwareAbstractionLayer hal, OperatingSystem operatingSystem, SpeedMeasurementManager speedMeasurementManager, Utils utils, Ticker ticker) {
        super(hal, operatingSystem, speedMeasurementManager, ticker, utils);
        this.ticker = ticker;
        this.monitorManagerFactory = monitorManagerFactory;
        this.hal = hal;
        this.operatingSystem = operatingSystem;
        this.speedMeasurementManager = speedMeasurementManager;
        this.utils = utils;
    }


    public boolean prerequisitesFilled() {
        return monitorManagerFactory.prerequisitesFilled();
    }


    @Override
    public void initialize() {
        boolean bridgeInitialized = monitorManagerFactory.initialize();
        if (bridgeInitialized) {
            monitorManager = monitorManagerFactory.getMonitorManager();
            setCpuMetrics(new WindowsCpuMetrics(hal, operatingSystem, monitorManager, ticker, utils));
            setNetworkMetrics(new WindowsNetworkMetrics(hal, speedMeasurementManager, monitorManager));
            setGpuMetrics(new WindowsGpuMetrics(hal, monitorManager));
            setDriveMetrics(new WindowsDriveMetrics(operatingSystem, hal, speedMeasurementManager));
            setProcessesMetrics(new DefaultProcessesMetrics(operatingSystem, hal));
            setMotherboardMetrics(new WindowsMotherboardMetrics(hal, monitorManager));
            setMemoryMetrics(new DefaultMemoryMetrics(hal, operatingSystem));
        }
    }
}
