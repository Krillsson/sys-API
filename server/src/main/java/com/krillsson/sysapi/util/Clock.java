package com.krillsson.sysapi.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Clock {
    private java.time.Clock clock = java.time.Clock.systemDefaultZone();
    private ZoneId zoneId = ZoneId.systemDefault();

    public ZonedDateTime now() {
        return ZonedDateTime.now(getClock());
    }

    public void useFixedClockAt(ZonedDateTime date) {
        clock = java.time.Clock.fixed(date.toInstant(), zoneId);
    }

    public void useSystemDefaultZoneClock() {
        clock = java.time.Clock.systemDefaultZone();
    }

    private java.time.Clock getClock() {
        return clock;
    }
}
