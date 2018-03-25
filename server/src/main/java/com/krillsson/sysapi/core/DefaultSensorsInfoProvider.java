package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.sensors.SensorsInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.PowerSource;

public class DefaultSensorsInfoProvider implements SensorsInfoProvider {

    private final HardwareAbstractionLayer hal;

    public DefaultSensorsInfoProvider(HardwareAbstractionLayer hal) {
        this.hal = hal;
    }

    @Override
    public SensorsInfo sensorsInfo() {
        return null;
    }

    public PowerSource[] powerSources() {
        return hal.getPowerSources();
    }

}
