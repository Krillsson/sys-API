package com.krillsson.sysapi.core.metrics.windows;

import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMotherboardMetrics;
import com.krillsson.sysapi.core.domain.sensors.DataType;
import com.krillsson.sysapi.core.domain.sensors.HealthData;
import com.krillsson.sysapi.util.Streams;
import ohmwrapper.MonitorManager;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WindowsMotherboardMetrics extends DefaultMotherboardMetrics {
    private final MonitorManager monitorManager;

    public WindowsMotherboardMetrics(HardwareAbstractionLayer hal, MonitorManager monitorManager) {
        super(hal);
        this.monitorManager = monitorManager;
    }

    @Override
    public List<HealthData> motherboardHealth() {
        return Optional.ofNullable(monitorManager.getMainboardMonitor()).map(mm -> {
            List<HealthData> healthData = new ArrayList<>();
            healthData.addAll(Streams.ofNullable(mm.getBoardTemperatures())
                                      .map(s -> new HealthData(s.getLabel(), s.getValue(), DataType.CELCIUS))
                                      .collect(Collectors.toList()));
            healthData.addAll(Streams.ofNullable(mm.getBoardFanPercent())
                                      .map(s -> new HealthData(s.getLabel(), s.getValue(), DataType.PERCENT))
                                      .collect(Collectors.toList()));
            healthData.addAll(Streams.ofNullable(mm.getBoardFanRPM())
                                      .map(s -> new HealthData(s.getLabel(), s.getValue(), DataType.RPM))
                                      .collect(Collectors.toList()));
            return healthData;
        }).orElse(Collections.emptyList());
    }
}
