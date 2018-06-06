package com.krillsson.sysapi.core.metrics.windows;

import com.krillsson.sysapi.core.TickManager;
import com.krillsson.sysapi.core.domain.cpu.CpuHealth;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultCpuMetrics;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultCpuSensors;
import com.krillsson.sysapi.core.metrics.windows.util.NullSafeOhmMonitor;
import com.krillsson.sysapi.util.Streams;
import com.krillsson.sysapi.util.Utils;
import ohmwrapper.MonitorManager;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static com.krillsson.sysapi.core.metrics.windows.util.NullSafeOhmMonitor.nullSafeGetValue;

public class WindowsCpuMetrics extends DefaultCpuMetrics {
    WindowsCpuMetrics(HardwareAbstractionLayer hal, OperatingSystem operatingSystem, DelegatingMonitorManager monitorManager, TickManager tickManager, Utils utils) {
        super(hal, operatingSystem, new WindowsCpuSensors(hal, monitorManager), utils, tickManager);
    }

    private static class WindowsCpuSensors extends DefaultCpuSensors {
        private final DelegatingMonitorManager monitorManager;

        WindowsCpuSensors(HardwareAbstractionLayer hal, DelegatingMonitorManager monitorManager) {
            super(hal);
            this.monitorManager = monitorManager;
        }

        @Override
        protected CpuHealth cpuHealth() {
            monitorManager.update();
            return super.cpuHealth();
        }

        @Override
        public List<Double> cpuTemperatures() {
            List<Double> temps = Streams.ofNullable(monitorManager.CpuMonitors())
                    .findFirst()
                    .map(cm -> Streams.ofNullable(cm.getTemperatures())
                            .mapToDouble(NullSafeOhmMonitor::nullSafeGetValue))
                    .orElse(DoubleStream.empty())
                    .boxed()
                    .collect(Collectors.toList());
            if (!temps.isEmpty()) {
                return temps;
            } else {
                return Collections.singletonList(Streams.ofNullable(monitorManager.CpuMonitors())
                                                         .findFirst()
                                                         .map(cm -> nullSafeGetValue(cm.getPackageTemperature()))
                                                         .orElse(-1d));
            }
        }

        @Override
        public double cpuFanRpm() {
            return Streams.ofNullable(monitorManager.CpuMonitors())
                    .findFirst()
                    .map(cm -> nullSafeGetValue(cm.getFanRPM())).orElse(-1d);
        }

        @Override
        public double cpuFanPercent() {
            return Streams.ofNullable(monitorManager.CpuMonitors())
                    .findFirst()
                    .map(cm -> nullSafeGetValue(cm.getFanPercent())).orElse(-1d);
        }

        @Override
        protected double cpuVoltage() {
            return Streams.ofNullable(monitorManager.CpuMonitors())
                    .findFirst()
                    .map(cm -> nullSafeGetValue(cm.getVoltage())).orElse(-1d);
        }
    }
}
