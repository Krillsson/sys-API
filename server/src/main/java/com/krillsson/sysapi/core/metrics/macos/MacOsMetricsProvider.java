package com.krillsson.sysapi.core.metrics.macos;

import com.krillsson.sysapi.util.Ticker;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMetrics;
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager;
import com.krillsson.sysapi.util.Utils;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

public class MacOsMetricsProvider extends DefaultMetrics {

    private final HardwareAbstractionLayer hal;
    private final OperatingSystem operatingSystem;
    private final SpeedMeasurementManager speedMeasurementManager;

    public MacOsMetricsProvider(HardwareAbstractionLayer hal, OperatingSystem operatingSystem, SpeedMeasurementManager speedMeasurementManager, Ticker ticker, Utils utils) {
        super(hal, operatingSystem, speedMeasurementManager, ticker, utils);
        this.hal = hal;
        this.operatingSystem = operatingSystem;
        this.speedMeasurementManager = speedMeasurementManager;
    }

    @Override
    public void initialize() {
        super.initialize();
        // this is a not so clean solution since super.initialize will first set DefaultCpuMetrics
        // and then this directly overwrites that variable
        setDriveMetrics(new MacOsDriveMetrics(operatingSystem, hal, speedMeasurementManager));
    }
}
