package com.krillsson.sysapi.core.metrics;

import com.krillsson.sysapi.core.domain.motherboard.Motherboard;
import com.krillsson.sysapi.core.domain.sensors.HealthData;

import java.util.List;

public interface MotherboardMetrics {
    Motherboard motherboard();

    List<HealthData> motherboardHealth();
}