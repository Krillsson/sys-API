package com.krillsson.sysapi.core.metrics.rasbian;

import com.krillsson.sysapi.core.SpeedMeasurementManager;
import com.krillsson.sysapi.core.metrics.defaultimpl.*;
import com.krillsson.sysapi.util.Utils;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

public class RaspbianMetricsFactory extends DefaultMetricsFactory {

    private final HardwareAbstractionLayer hal;
    private final OperatingSystem operatingSystem;
    private final Utils utils;

    public RaspbianMetricsFactory(HardwareAbstractionLayer hal, OperatingSystem operatingSystem, SpeedMeasurementManager speedMeasurementManager, Utils utils) {
        super(hal, operatingSystem, speedMeasurementManager, utils);
        this.hal = hal;
        this.operatingSystem = operatingSystem;
        this.utils = utils;
    }


    @Override
    public boolean initialize() {
        super.initialize();
        // this is a not so clean solution since super.initialize will first set DefaultCpuMetrics
        // and then this directly overwrites that variable
        setCpuMetrics(new RaspbianCpuMetrics(hal, operatingSystem, utils));
        return true;
    }
}
