package com.krillsson.sysapi.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Clock {
    private java.time.Clock clock = java.time.Clock.systemDefaultZone();
    private ZoneId zoneId = ZoneId.systemDefault();

    public LocalDateTime now() {
        return LocalDateTime.now(getClock());
    }

    public void useFixedClockAt(LocalDateTime date) {
        clock = java.time.Clock.fixed(date.atZone(zoneId).toInstant(), zoneId);
    }

    public void useSystemDefaultZoneClock() {
        clock = java.time.Clock.systemDefaultZone();
    }

    private java.time.Clock getClock() {
        return clock;
    }

}
