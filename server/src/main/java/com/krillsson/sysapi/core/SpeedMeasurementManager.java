package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.network.SpeedMeasurement;
import com.krillsson.sysapi.util.Utils;
import io.dropwizard.lifecycle.Managed;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpeedMeasurementManager implements Managed {

    interface SpeedSource {
        String getName();

        long getCurrentRead();

        long getCurrentWrite();
    }

    static class CurrentSpeed {
        private final long readPerSeconds;
        private final long writePerSeconds;

        CurrentSpeed(long readPerSeconds, long writePerSeconds) {
            this.readPerSeconds = readPerSeconds;
            this.writePerSeconds = writePerSeconds;
        }

        public long getReadPerSeconds() {
            return readPerSeconds;
        }

        public long getWritePerSeconds() {
            return writePerSeconds;
        }
    }

    private final ScheduledExecutorService executorService;
    private final Clock clock;

    private final HashMap<String, SpeedMeasurement> speedMeasurementStore = new HashMap<>();
    private final HashMap<String, CurrentSpeed> currentSpeedHashMap = new HashMap<>();

    private final List<SpeedSource> speedSources = new ArrayList<>();


    public SpeedMeasurementManager(ScheduledExecutorService executorService, Clock clock) {
        this.executorService = executorService;
        this.clock = clock;
    }

    void register(SpeedSource speedSource){
        speedSources.add(speedSource);
    }

    void 

    void initialize() {
        for (SpeedSource speedSource : speedSources) {
            speedMeasurementStore.put(speedSource.getName(), new SpeedMeasurement(speedSource.getCurrentRead(), speedSource.getCurrentWrite(), LocalDateTime.now(clock)));
        }
    }

    private void execute() {
        for (SpeedSource speedSource : speedSources) {
            SpeedMeasurement start = speedMeasurementStore.get(speedSource.getName());

            SpeedMeasurement end = new SpeedMeasurement(speedSource.getCurrentRead(), speedSource.getCurrentWrite(), LocalDateTime.now(clock));

            final long readPerSecond = measureSpeed(start.getSampledAt(), end.getSampledAt(), start.getRead(), end.getRead());
            final long writePerSecond = measureSpeed(start.getSampledAt(), end.getSampledAt(), start.getWrite(), end.getWrite());

            currentSpeedHashMap.put(speedSource.getName(), new CurrentSpeed(readPerSecond, writePerSecond));
            speedMeasurementStore.put(speedSource.getName(), end);
        }
    }


    private long measureSpeed(LocalDateTime start, LocalDateTime end, long valueStart, long valueEnd) {
        double duration = Duration.between(start, end).getSeconds();
        double deltaBits = (valueEnd - valueStart);
        if(deltaBits <= 0 || duration <= 0){
            return 0L;
        }
        double bitsPerSecond = deltaBits / duration;
        return (long)bitsPerSecond;
    }

    @Override
    public void start() throws Exception {
        executorService.scheduleAtFixedRate(this::execute, 1, Duration.ofSeconds(5).getSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public void stop() throws Exception {
        executorService.shutdownNow();
    }
}
