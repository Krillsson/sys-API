package com.krillsson.sysapi.core.metrics.defaultimpl;

import com.krillsson.sysapi.core.metrics.MotherboardMetrics;
import com.krillsson.sysapi.core.domain.motherboard.Motherboard;
import com.krillsson.sysapi.core.domain.sensors.HealthData;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.Collections;
import java.util.List;

public class DefaultMotherboardMetrics implements MotherboardMetrics {

    private final HardwareAbstractionLayer hal;

    public DefaultMotherboardMetrics(HardwareAbstractionLayer hal) {
        this.hal = hal;
    }

    @Override
    public Motherboard motherboard() {
        return new Motherboard(hal.getComputerSystem(), hal.getUsbDevices(false));
    }

    @Override
    public List<HealthData> motherboardHealth() {
        return Collections.emptyList();
    }
}
