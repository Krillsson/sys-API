package com.krillsson.sysapi.core.monitoring;

import com.krillsson.sysapi.core.monitoring.monitors.*;
import com.krillsson.sysapi.dto.monitor.Monitor;
import com.krillsson.sysapi.dto.monitor.MonitorEvent;
import com.krillsson.sysapi.dto.monitor.MonitorStatus;
import com.krillsson.sysapi.dto.monitor.MonitorType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MonitorMapper {
    MonitorMapper INSTANCE = Mappers.getMapper(MonitorMapper.class);

    default Monitor map(com.krillsson.sysapi.core.monitoring.Monitor monitor) {
        MonitorType type = null;
        if (monitor instanceof CpuMonitor) {
            type = MonitorType.CPU;
        } else if (monitor instanceof DriveMonitor) {
            type = MonitorType.DRIVE;
        } else if (monitor instanceof NetworkUpMonitor) {
            type = MonitorType.NETWORK_UP;
        } else if (monitor instanceof MemoryMonitor) {
            type = MonitorType.MEMORY;
        } else if (monitor instanceof GpuMonitor) {
            type = MonitorType.GPU;
        } else if (monitor instanceof CpuTemperatureMonitor) {
            type = MonitorType.CPU_TEMP;
        }
        return new Monitor(monitor.id(), monitor.inertia().getSeconds(), type, monitor.threshold());
    }

    List<Monitor> mapList(List<com.krillsson.sysapi.core.monitoring.Monitor> values);

    MonitorStatus map(com.krillsson.sysapi.core.monitoring.MonitorEvent.MonitorStatus value);

    default MonitorType map(com.krillsson.sysapi.core.monitoring.MonitorType value) {
        switch (value) {
            case CPU_LOAD:
                return MonitorType.CPU;
            case CPU_TEMP:
                return MonitorType.CPU_TEMP;
            case DRIVE_SPACE:
                return MonitorType.DRIVE;
            case DRIVE_TEMP:
                return null;
            case GPU_LOAD:
                return MonitorType.GPU;
            case GPU_TEMP:
                return null;
            case MEMORY_SPACE:
                return MonitorType.MEMORY;
            case NETWORK_UP:
                return MonitorType.NETWORK_UP;
        }
        return null;
    }

    default MonitorEvent map(com.krillsson.sysapi.core.monitoring.MonitorEvent event) {
        return new MonitorEvent(
                event.getId().toString(), event.getMonitorId(), map(event.getTime()),
                INSTANCE.map(event.getMonitorStatus()),
                INSTANCE.map(event.getMonitorType()),
                event.getThreshold(),
                event.getValue()
        );
    }

    default com.krillsson.sysapi.core.monitoring.Monitor map(Monitor monitor) {
        switch (monitor.getType()) {
            case CPU:
                return new CpuMonitor(
                        monitor.getId(),
                        Duration.ofSeconds(monitor.getInertiaInSeconds()),
                        monitor.getThreshold()
                );
            case CPU_TEMP:
                return new CpuTemperatureMonitor(
                        monitor.getId(),
                        Duration.ofSeconds(monitor.getInertiaInSeconds()),
                        monitor.getThreshold()
                );
            case DRIVE:
                return new DriveMonitor(
                        monitor.getId(),
                        Duration.ofSeconds(monitor.getInertiaInSeconds()),
                        monitor.getThreshold().longValue()
                );
            case GPU:
                return new GpuMonitor(
                        monitor.getId(),
                        Duration.ofSeconds(monitor.getInertiaInSeconds()),
                        monitor.getThreshold().longValue()
                );
            case MEMORY:
                return new MemoryMonitor(
                        monitor.getId(),
                        Duration.ofSeconds(monitor.getInertiaInSeconds()),
                        monitor.getThreshold().longValue()
                );
            case NETWORK_UP:
                return new NetworkUpMonitor(
                        monitor.getId(),
                        Duration.ofSeconds(monitor.getInertiaInSeconds()),
                        monitor.getThreshold().longValue()
                );
        }
        return null;
    }

    default String map(OffsetDateTime value) {
        return value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    List<MonitorEvent> map(List<com.krillsson.sysapi.core.monitoring.MonitorEvent> events);
}
