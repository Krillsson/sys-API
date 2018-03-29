package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.cpu.CoreLoad;
import com.krillsson.sysapi.core.domain.cpu.CpuInfo;
import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.util.Utils;
import org.slf4j.Logger;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultCpuInfoProvider implements CpuInfoProvider {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DefaultInfoProvider.class);

    private final HardwareAbstractionLayer hal;
    private final OperatingSystem operatingSystem;
    private final Utils utils;
    private final DefaultCpuSensors cpuSensors;
    private static final long MAX_SAMPLING_THRESHOLD = TimeUnit.SECONDS.toMillis(10);
    private static final int SLEEP_SAMPLE_PERIOD = 1000;
    private long coreTicksSampledAt = -1;
    private long[][] coreTicks = new long[0][0];

    protected DefaultCpuInfoProvider(HardwareAbstractionLayer hal, OperatingSystem operatingSystem, DefaultCpuSensors cpuSensors, Utils utils) {
        this.hal = hal;
        this.operatingSystem = operatingSystem;
        this.cpuSensors = cpuSensors;
        this.utils = utils;
    }

    @Override
    public CpuInfo cpuInfo() {
        return new CpuInfo(
                hal.getProcessor());
    }

    @Override
    public CpuLoad cpuLoad() {
        CentralProcessor processor = hal.getProcessor();
        if (Arrays.equals(coreTicks, new long[0][0]) || utils.isOutsideMaximumDuration(
                coreTicksSampledAt,
                MAX_SAMPLING_THRESHOLD
        )) {
            LOGGER.debug("Sleeping thread since we don't have enough sample data. Hold on!");
            coreTicks = processor.getProcessorCpuLoadTicks();
            coreTicksSampledAt = utils.currentSystemTime();
            utils.sleep(SLEEP_SAMPLE_PERIOD);
        }
        CoreLoad[] coreLoads = new CoreLoad[processor.getLogicalProcessorCount()];
        long[][] currentProcessorTicks = processor.getProcessorCpuLoadTicks();
        long sampledAt = utils.currentSystemTime();
        for (int i = 0; i < coreLoads.length; i++) {
            long[] currentTicks = currentProcessorTicks[i];
            long user = currentTicks[CentralProcessor.TickType.USER.getIndex()] - coreTicks[i][CentralProcessor.TickType.USER.getIndex()];
            long nice = currentTicks[CentralProcessor.TickType.NICE.getIndex()] - coreTicks[i][CentralProcessor.TickType.NICE.getIndex()];
            long sys = currentTicks[CentralProcessor.TickType.SYSTEM.getIndex()] - coreTicks[i][CentralProcessor.TickType.SYSTEM.getIndex()];
            long idle = currentTicks[CentralProcessor.TickType.IDLE.getIndex()] - coreTicks[i][CentralProcessor.TickType.IDLE.getIndex()];
            long iowait = currentTicks[CentralProcessor.TickType.IOWAIT.getIndex()] - coreTicks[i][CentralProcessor.TickType.IOWAIT.getIndex()];
            long irq = currentTicks[CentralProcessor.TickType.IRQ.getIndex()] - coreTicks[i][CentralProcessor.TickType.IRQ.getIndex()];
            long softirq = currentTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - coreTicks[i][CentralProcessor.TickType.SOFTIRQ.getIndex()];
            long steal = currentTicks[CentralProcessor.TickType.STEAL.getIndex()] - coreTicks[i][CentralProcessor.TickType.STEAL.getIndex()];

            long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;
            //long totalIdle = idle + iowait;
            //long totalSystem = irq + softirq + sys + steal;
            if (totalCpu != 0L) {
                coreLoads[i] = new CoreLoad(
                        Utils.round(100d * user / totalCpu, 2),
                        Utils.round(100d * nice / totalCpu, 2),
                        Utils.round(100d * sys / totalCpu, 2),
                        Utils.round(100d * idle / totalCpu, 2),
                        Utils.round(100d * iowait / totalCpu, 2),
                        Utils.round(100d * irq / totalCpu, 2),
                        Utils.round(100d * softirq / totalCpu, 2),
                        Utils.round(100d * steal / totalCpu, 2)
                );
            } else {
                coreLoads[i] = new CoreLoad(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
                LOGGER.debug("Something went wrong with reading CPU core load");
            }
        }

        coreTicks = currentProcessorTicks;
        coreTicksSampledAt = sampledAt;

        return new CpuLoad(
                Utils.round(processor.getSystemCpuLoadBetweenTicks() * 100d, 2),
                Utils.round(processor.getSystemCpuLoad() * 100d, 2),
                Stream.of(coreLoads).collect(Collectors.toList()), cpuSensors.cpuHealth(), operatingSystem.getProcessCount(), operatingSystem.getThreadCount()
        );
    }
}