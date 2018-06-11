package com.krillsson.sysapi.util;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeMachine {
    private Clock clock = Clock.systemDefaultZone();
    private ZoneId zoneId = ZoneId.systemDefault();

    public LocalDateTime now() {
        return LocalDateTime.now(getClock());
    }

    public void useFixedClockAt(LocalDateTime date) {
        clock = Clock.fixed(date.atZone(zoneId).toInstant(), zoneId);
    }

    public void useSystemDefaultZoneClock() {
        clock = Clock.systemDefaultZone();
    }

    private Clock getClock() {
        return clock;
    }

}
