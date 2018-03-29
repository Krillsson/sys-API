package com.krillsson.sysapi.core.windows;

import com.krillsson.sysapi.core.DefaultCpuInfoProvider;
import com.krillsson.sysapi.core.DefaultCpuSensors;
import com.krillsson.sysapi.core.domain.cpu.CpuHealth;
import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.core.windows.util.NullSafeOhmMonitor;
import com.krillsson.sysapi.util.Streams;
import com.krillsson.sysapi.util.Utils;
import ohmwrapper.CpuMonitor;
import ohmwrapper.MonitorManager;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static com.krillsson.sysapi.core.windows.util.NullSafeOhmMonitor.nullSafe;
import static com.krillsson.sysapi.core.windows.util.NullSafeOhmMonitor.nullSafeGetValue;

public class WindowsCpuInfoProvider extends DefaultCpuInfoProvider {
    WindowsCpuInfoProvider(HardwareAbstractionLayer hal, OperatingSystem operatingSystem, MonitorManager monitorManager, Utils utils) {
        super(hal, operatingSystem, new WindowsCpuSensors(hal, monitorManager), utils);
    }

    private static class WindowsCpuSensors extends DefaultCpuSensors {
        private final MonitorManager monitorManager;

        WindowsCpuSensors(HardwareAbstractionLayer hal, MonitorManager monitorManager) {
            super(hal);
            this.monitorManager = monitorManager;
        }

        @Override
        protected CpuHealth cpuHealth() {
            monitorManager.Update();
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
