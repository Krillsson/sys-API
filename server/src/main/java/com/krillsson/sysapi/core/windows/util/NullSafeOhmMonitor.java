package com.krillsson.sysapi.core.windows.util;

import ohmwrapper.OHMSensor;

public class NullSafeOhmMonitor {
    public static OHMSensor nullSafe(OHMSensor sensor) {
        if (sensor != null) {
            return sensor;
        } else return new NullSafeOHMSensor();
    }

    public static OHMSensor[] nullSafe(OHMSensor[] arr) {
        if (arr != null) {
            return arr;
        } else {
            return new OHMSensor[0];
        }
    }

    public static class NullSafeOHMSensor extends OHMSensor {
        public NullSafeOHMSensor() {
            super(null, null, null, false);
        }

        @Override
        public String Text() {
            return "N/A";
        }

        @Override
        public double getValue() {
            return -1;
        }
    }
}
