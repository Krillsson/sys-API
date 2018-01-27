package com.krillsson.sysapi.util;

public class Utils {
    public long currentSystemTime() {
        return System.currentTimeMillis();
    }

    public void sleep(long ms) {
        oshi.util.Util.sleep(ms);
    }

    public boolean isOutsideMaximumDuration(long sampleTimeStamp, long maxSampleThreshold) {
        return currentSystemTime() - sampleTimeStamp > maxSampleThreshold;
    }
}
