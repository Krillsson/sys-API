package com.krillsson.sysapi.core.monitor;

import com.krillsson.sysapi.dto.monitor.Monitor;
import com.krillsson.sysapi.dto.monitor.MonitorType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Duration;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MonitorMapper {
    MonitorMapper INSTANCE = Mappers.getMapper(MonitorMapper.class);

    default Monitor map(com.krillsson.sysapi.core.monitor.Monitor monitor) {
        MonitorType type = null;
        if (monitor instanceof CpuMonitor) {
            type = MonitorType.CPU;
        } else if (monitor instanceof DriveMonitor) {
            type = MonitorType.DRIVE;
        }
        return new Monitor(monitor.id(), monitor.inertia().getSeconds(), type, monitor.threshold());
    }

    default Monitor map(DriveMonitor monitor) {
        return new Monitor(
                monitor.id(),
                monitor.inertia().getSeconds(),
                MonitorType.DRIVE,
                monitor.threshold()
        );
    }

    default Monitor map(CpuMonitor monitor) {
        return new Monitor(monitor.id(), monitor.inertia().getSeconds(), MonitorType.CPU, monitor.threshold());
    }

    default com.krillsson.sysapi.core.monitor.Monitor map(Monitor monitor) {
        switch (monitor.getType()) {
            case CPU:
                return new CpuMonitor(
                        monitor.getId(),
                        Duration.ofSeconds(monitor.getInertiaInSeconds()),
                        monitor.getThreshold()
                );
            case CPU_TEMP:
                break;
            case DRIVE:
                return new DriveMonitor(
                        monitor.getId(),
                        Duration.ofSeconds(monitor.getInertiaInSeconds()),
                        monitor.getThreshold().longValue()
                );
            case GPU:
                break;
            case MEMORY:
                break;
            case NETWORK:
                break;
        }
        return null;
    }
}
