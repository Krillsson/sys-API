package com.krillsson.sysapi.core.history;

import com.krillsson.sysapi.util.Clock;
import org.junit.Before;
import org.junit.Test;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class HistoryTest {

    History<Object> history;
    Clock clock;


    @Before
    public void setUp() throws Exception {
        clock = new Clock();
        history = new History<>(clock);

    }

    @Test
    public void happyPath() {

        OffsetDateTime twoMinutesAgo = OffsetDateTime.now().minusMinutes(2);
        clock.useFixedClockAt(twoMinutesAgo);
        history.record(new Object());
        clock.useFixedClockAt(twoMinutesAgo.plusMinutes(4));
        history.record(new Object());

        assertThat(history.get().size(), is(2));
        assertTrue(history.get().get(0).getDate().isBefore(history.get().get(1).getDate()));
    }

    @Test
    public void purgingRemovesStuff() {
        OffsetDateTime twoMinutesAgo = OffsetDateTime.now().minusMinutes(2);

        System.out.println(OffsetDateTime.now().minusDays(2).toString());


        System.out.println(OffsetDateTime.now().plusHours(2).toString());
        clock.useFixedClockAt(twoMinutesAgo);
        history.record(new Object());
        clock.useFixedClockAt(twoMinutesAgo.plusMinutes(4));
        history.record(new Object());

        assertThat(history.get().size(), is(2));

        history.purge(1, ChronoUnit.MINUTES);

        assertThat(history.get().size(), is(1));
    }
}