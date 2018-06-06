package com.krillsson.sysapi.core.history;

import org.junit.Before;
import org.junit.Test;

import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class HistoryTest {

    History<Object> history;

    @Before
    public void setUp() throws Exception {
        history = new History<>();
    }

    @Test
    public void happyPath() {

        history.record(new Object());
        history.record(new Object());

        assertThat(history.get().size(), is(2));
        assertTrue(history.get().get(0).date.isBefore(history.get().get(1).date));
    }

    @Test
    public void purgingRemovesStuff() {
        history.record(new Object());
        history.record(new Object());

        assertThat(history.get().size(), is(2));

        history.purge(1, ChronoUnit.NANOS);

        assertThat(history.get().size(), is(0));
    }
}