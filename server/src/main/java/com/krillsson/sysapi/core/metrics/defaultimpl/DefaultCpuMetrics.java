package com.krillsson.sysapi.core.metrics.defaultimpl;

import com.krillsson.sysapi.core.domain.cpu.CentralProcessor;
import com.krillsson.sysapi.core.domain.cpu.CoreLoad;
import com.krillsson.sysapi.core.domain.cpu.CpuInfo;
import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.core.metrics.CpuMetrics;
import com.krillsson.sysapi.util.Ticker;
import com.krillsson.sysapi.util.Utils;
import org.slf4j.Logger;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultCpuMetrics implements CpuMetrics, Ticker.TickListener {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DefaultCpuMetrics.class);
    private static final int SLEEP_SAMPLE_PERIOD = 1000;
    private final HardwareAbstractionLayer hal;
    private final OperatingSystem operatingSystem;
    private final Utils utils;
    private final Ticker ticker;
    private final DefaultCpuSensors cpuSensors;
    private long[][] coreTicks;
    private long[] ticks;
    private CpuLoad cpuLoad;

    public DefaultCpuMetrics(HardwareAbstractionLayer hal, OperatingSystem operatingSystem, DefaultCpuSensors cpuSensors, Utils utils, Ticker ticker) {
        this.hal = hal;
        this.operatingSystem = operatingSystem;
        this.cpuSensors = cpuSensors;
        this.utils = utils;
        this.ticker = ticker;
    }

    void register() {
        ticker.register(this);
    }

    void unregister() {
        ticker.unregister(this);
    }

    @Override
    public CpuInfo cpuInfo() {
        oshi.hardware.CentralProcessor processor = hal.getProcessor();
        oshi.hardware.CentralProcessor.ProcessorIdentifier identifier = processor.getProcessorIdentifier();
        CentralProcessor centralProcessor = new CentralProcessor(processor.getLogicalProcessorCount(),
                processor.getPhysicalProcessorCount(),
                identifier.getName(),
                identifier.getIdentifier(),
                identifier.getFamily(),
                identifier.getVendor(),
                identifier.getVendorFreq(),
                identifier.getModel(),
                identifier.getStepping(),
                identifier.isCpu64bit());
        return new CpuInfo(centralProcessor);
    }

    @Override
    public CpuLoad cpuLoad() {
        return cpuLoad;
    }

    @Override
    public long uptime() {
        return operatingSystem.getSystemUptime();
    }

    @Override
    public void onTick() {
        oshi.hardware.CentralProcessor processor = hal.getProcessor();
        if (coreTicks == null) {
            coreTicks = processor.getProcessorCpuLoadTicks();
            ticks = processor.getSystemCpuLoadTicks();
            utils.sleep(SLEEP_SAMPLE_PERIOD);
        }
        CoreLoad[] coreLoads = new CoreLoad[processor.getLogicalProcessorCount()];
        long[][] currentProcessorTicks = processor.getProcessorCpuLoadTicks();
        for (int i = 0; i < coreLoads.length; i++) {
            long[] currentTicks = currentProcessorTicks[i];
            long user = currentTicks[oshi.hardware.CentralProcessor.TickType.USER.getIndex()] - coreTicks[i][oshi.hardware.CentralProcessor.TickType.USER
                    .getIndex()];
            long nice = currentTicks[oshi.hardware.CentralProcessor.TickType.NICE.getIndex()] - coreTicks[i][oshi.hardware.CentralProcessor.TickType.NICE
                    .getIndex()];
            long sys = currentTicks[oshi.hardware.CentralProcessor.TickType.SYSTEM.getIndex()] - coreTicks[i][oshi.hardware.CentralProcessor.TickType.SYSTEM
                    .getIndex()];
            long idle = currentTicks[oshi.hardware.CentralProcessor.TickType.IDLE.getIndex()] - coreTicks[i][oshi.hardware.CentralProcessor.TickType.IDLE
                    .getIndex()];
            long iowait = currentTicks[oshi.hardware.CentralProcessor.TickType.IOWAIT.getIndex()] - coreTicks[i][oshi.hardware.CentralProcessor.TickType.IOWAIT
                    .getIndex()];
            long irq = currentTicks[oshi.hardware.CentralProcessor.TickType.IRQ.getIndex()] - coreTicks[i][oshi.hardware.CentralProcessor.TickType.IRQ
                    .getIndex()];
            long softirq = currentTicks[oshi.hardware.CentralProcessor.TickType.SOFTIRQ.getIndex()] - coreTicks[i][oshi.hardware.CentralProcessor.TickType.SOFTIRQ
                    .getIndex()];
            long steal = currentTicks[oshi.hardware.CentralProcessor.TickType.STEAL.getIndex()] - coreTicks[i][oshi.hardware.CentralProcessor.TickType.STEAL
                    .getIndex()];

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

        this.cpuLoad = new CpuLoad(
                Utils.round(processor.getSystemCpuLoadBetweenTicks(ticks) * 100d, 2),
                Utils.round(0, 2),
                Utils.round(processor.getSystemLoadAverage(1)[0] * 100d, 2),
                Stream.of(coreLoads).collect(Collectors.toList()),
                cpuSensors.cpuHealth(),
                operatingSystem.getProcessCount(),
                operatingSystem.getThreadCount()
        );
        ticks = processor.getSystemCpuLoadTicks();
    }
}
