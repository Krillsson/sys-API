package com.krillsson.sysapi.util;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class Clock {
    private java.time.Clock clock = java.time.Clock.systemDefaultZone();
    private ZoneId zoneId = ZoneId.systemDefault();

    public OffsetDateTime offsetDateTime() {
        return OffsetDateTime.now(getClock());
    }

    public Instant instant() {
        return Instant.now(getClock());
    }

    public void useFixedClockAt(OffsetDateTime date) {
        clock = java.time.Clock.fixed(date.toInstant(), zoneId);
    }

    public void useSystemDefaultZoneClock() {
        clock = java.time.Clock.systemDefaultZone();
    }

    private java.time.Clock getClock() {
        return clock;
    }
}
