package com.krillsson.sysapi.core.linux.rasbian;

import com.google.common.annotations.VisibleForTesting;
import com.krillsson.sysapi.core.DefaultCpuInfoProvider;
import com.krillsson.sysapi.core.DefaultCpuSensors;
import com.krillsson.sysapi.util.Utils;
import org.slf4j.Logger;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import oshi.util.ExecutingCommand;
import oshi.util.FileUtil;

import java.util.Collections;
import java.util.List;

public class RaspbianCpuInfoProvider extends DefaultCpuInfoProvider {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(RaspbianLinuxInfoProvider.class);

    public static final String RASPBIAN_QUALIFIER = "raspbian";

    private static final String CPU_TEMP_FILE_LOCATION = "/sys/class/thermal/thermal_zone0/temp";
    private static final String VCGENCMD = "vcgencmd";
    private static final String VCGENCMD_VOLT = VCGENCMD + "measure_volts core";


    protected RaspbianCpuInfoProvider(HardwareAbstractionLayer hal, OperatingSystem operatingSystem, Utils utils) {
        super(hal, operatingSystem, new RaspbianCpuSensors(hal), utils);
    }

    private static class RaspbianCpuSensors extends DefaultCpuSensors {

        public RaspbianCpuSensors(HardwareAbstractionLayer hal) {
            super(hal);
        }

        @Override
        protected List<Double> cpuTemperatures() {
            return Collections.singletonList(FileUtil.getLongFromFile(CPU_TEMP_FILE_LOCATION) / 1000.0);
        }

        @Override
        protected double cpuVoltage() {
            String vcgenCmdAnswer = executeCommand();
            if (vcgenCmdAnswer != null && vcgenCmdAnswer.length() > 1) {
                String trimmed = vcgenCmdAnswer.replace("volt=", "").replace("V", "");
                try {
                    Double value = Double.valueOf(trimmed);
                    if (value.isInfinite() || value.isNaN()) {
                        return 0.0;
                    } else {
                        return value;
                    }
                } catch (NumberFormatException e) {
                    LOGGER.error("Error while parsing vcgencmd command", e);
                    return 0.0;
                }
            }
            return super.cpuVoltage();
        }

        @VisibleForTesting
        String executeCommand() {
            return ExecutingCommand.getFirstAnswer(VCGENCMD_VOLT);
        }
    }
}
