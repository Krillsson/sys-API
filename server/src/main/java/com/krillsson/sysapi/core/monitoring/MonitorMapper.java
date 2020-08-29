package com.krillsson.sysapi.core.monitoring;

import com.krillsson.sysapi.core.domain.event.EventType;
import com.krillsson.sysapi.core.monitoring.monitors.CpuMonitor;
import com.krillsson.sysapi.core.monitoring.monitors.CpuTemperatureMonitor;
import com.krillsson.sysapi.core.monitoring.monitors.DriveMonitor;
import com.krillsson.sysapi.core.monitoring.monitors.*;
import com.krillsson.sysapi.dto.monitor.Monitor;
import com.krillsson.sysapi.dto.monitor.MonitorEvent;
import com.krillsson.sysapi.dto.monitor.MonitorStatus;
import com.krillsson.sysapi.dto.monitor.MonitorType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

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
        } else if (monitor instanceof CpuTemperatureMonitor) {
            type = MonitorType.CPU_TEMP;
        }
        return new Monitor(monitor.getId().toString(), monitor.getConfig().getInertia().getSeconds(), type, monitor.getConfig().getThreshold());
    }

    List<Monitor> mapList(List<com.krillsson.sysapi.core.monitoring.Monitor> values);

    MonitorStatus map(EventType value);

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

    default com.krillsson.sysapi.core.monitoring.MonitorType map(MonitorType value) {
        switch (value) {
            case CPU:
                return com.krillsson.sysapi.core.monitoring.MonitorType.CPU_LOAD;
            case CPU_TEMP:
                return com.krillsson.sysapi.core.monitoring.MonitorType.CPU_TEMP;
            case DRIVE:
                return com.krillsson.sysapi.core.monitoring.MonitorType.DRIVE_SPACE;
            case GPU:
                return com.krillsson.sysapi.core.monitoring.MonitorType.GPU_LOAD;
            case MEMORY:
                return com.krillsson.sysapi.core.monitoring.MonitorType.MEMORY_SPACE;
            case NETWORK_UP:
                return com.krillsson.sysapi.core.monitoring.MonitorType.NETWORK_UP;
        }
        return null;
    }

    default MonitorEvent map(com.krillsson.sysapi.core.domain.event.MonitorEvent event) {
        return new MonitorEvent(
                event.getId().toString(), event.getMonitorId().toString(), map(event.getTime()),
                INSTANCE.map(event.getEventType()),
                INSTANCE.map(event.getMonitorType()),
                event.getThreshold(),
                event.getValue()
        );
    }

    default String map(OffsetDateTime value) {
        return value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    List<MonitorEvent> map(List<com.krillsson.sysapi.core.domain.event.MonitorEvent> events);
}
