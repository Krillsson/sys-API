package com.krillsson.sysapi.util;

import oshi.util.Util;

public class Utils {
    public long currentSystemTime() {
        return System.currentTimeMillis();
    }

    public void sleep(long ms) {
        oshi.util.Util.sleep(ms);
    }

    public void sleepAfter(long startTime, long ms) {
        oshi.util.Util.sleepAfter(startTime, ms);
    }

    public boolean isOutsideMaximumDuration(long sampleTimeStamp, long maxSampleThreshold) {
        return currentSystemTime() - sampleTimeStamp > maxSampleThreshold;
    }

    public boolean isInsideMinimumDuration(long sampledAt, long minSamplingThreshold) {
        return currentSystemTime() - sampledAt < minSamplingThreshold;
    }
}
