package com.krillsson.sysapi.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils {
    public static double round(double value, int places) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            return 0d;
        }
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
