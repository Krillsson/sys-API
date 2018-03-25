package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.sensors.SensorsInfo;
import oshi.hardware.PowerSource;

public interface SensorsInfoProvider {
    SensorsInfo sensorsInfo();

    PowerSource[] powerSources();
}
