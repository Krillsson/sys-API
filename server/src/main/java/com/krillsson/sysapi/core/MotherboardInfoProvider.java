package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.motherboard.Motherboard;
import com.krillsson.sysapi.core.domain.sensors.HealthData;

public interface MotherboardInfoProvider {
    Motherboard motherboard();

    HealthData[] mainboardHealthData();
}
