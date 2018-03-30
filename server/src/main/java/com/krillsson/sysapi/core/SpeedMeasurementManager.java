package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.network.SpeedMeasurement;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpeedMeasurementManager implements Managed {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SpeedMeasurementManager.class);
    private final long measurementInterval;

    public interface SpeedSource {
        String getName();

        long getCurrentRead();

        long getCurrentWrite();
    }

    public static class CurrentSpeed {
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
    private final HashMap<String, CurrentSpeed> currentSpeedStore = new HashMap<>();

    private final List<SpeedSource> speedSources = new ArrayList<>();

    public SpeedMeasurementManager(ScheduledExecutorService executorService, Clock clock, int measurementInterval) {
        this.executorService = executorService;
        this.clock = clock;
        this.measurementInterval = Duration.ofSeconds(measurementInterval).getSeconds();
    }

    public void register(Collection<SpeedSource> sources){
        LOGGER.debug("Registering {}", sources.parallelStream().map(SpeedSource::getName).toArray());
        speedSources.addAll(sources);
    }

    public void register(SpeedSource speedSource){
        LOGGER.debug("Registering {}", speedSource.getName());
        speedSources.add(speedSource);
    }

    public void unregister(SpeedSource speedSource){
        speedSources.remove(speedSource);
    }

    public Optional<CurrentSpeed> getCurrentSpeedForName(String name){
        return Optional.ofNullable(currentSpeedStore.get(name));
    }

    private void execute() {
        for (SpeedSource speedSource : speedSources) {
            SpeedMeasurement start = speedMeasurementStore.get(speedSource.getName());
            SpeedMeasurement end = new SpeedMeasurement(speedSource.getCurrentRead(), speedSource.getCurrentWrite(), LocalDateTime.now(clock));
            if(start != null){
                final long readPerSecond = measureSpeed(start.getSampledAt(), end.getSampledAt(), start.getRead(), end.getRead());
                final long writePerSecond = measureSpeed(start.getSampledAt(), end.getSampledAt(), start.getWrite(), end.getWrite());

                LOGGER.trace("Current speed for {}: read: {}/s write: {}/s", speedSource.getName(), readPerSecond, writePerSecond);

                currentSpeedStore.put(speedSource.getName(), new CurrentSpeed(readPerSecond, writePerSecond));
                speedMeasurementStore.put(speedSource.getName(), end);
            }
            else{
                LOGGER.debug("Initializing measurement for {}", speedSource.getName());
                speedMeasurementStore.put(speedSource.getName(), end);
            }

        }
    }

    private long measureSpeed(LocalDateTime start, LocalDateTime end, long valueStart, long valueEnd) {
        double duration = Duration.between(start, end).getSeconds();
        double deltaValue = (valueEnd - valueStart);
        if(deltaValue <= 0 || duration <= 0){
            return 0L;
        }
        double valuePerSecond = deltaValue / duration;
        return (long)valuePerSecond;
    }

    @Override
    public void start() throws Exception {
        executorService.scheduleAtFixedRate(this::execute, 1, measurementInterval, TimeUnit.SECONDS);
    }

    @Override
    public void stop() throws Exception {
        speedSources.clear();
        executorService.shutdownNow();
    }
}
