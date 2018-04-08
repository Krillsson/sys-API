package com.krillsson.sysapi.config;

import java.util.concurrent.TimeUnit;

public class HistoryConfiguration {
    private HistoryPurgingConfiguration purgingConfiguration;
    private long initialDelay;
    private long period;
    private TimeUnit periodUnit;

    public HistoryPurgingConfiguration getPurgingConfiguration() {
        return purgingConfiguration;
    }

    public long getInitialDelay() {
        return initialDelay;
    }

    public long getPeriod() {
        return period;
    }

    public TimeUnit getPeriodUnit() {
        return periodUnit;
    }
}
