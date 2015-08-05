package com.krillsson.sysapi.sigar;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class MemorySigarTest extends CheckSigarLoadsOk {
    private MemorySigar mm;

    @Before
    public void setUp() {
        mm = SigarKeeper.getInstance().memory();
    }

    @Test
    public void totalMemoryIsGreaterThanZero() throws Exception {
        assertThat(mm.getRam().total(), is(greaterThan(0L)));
    }

    @Test
    public void usedMemoryIsLessThanOrEqualToTotalMemory() throws Exception {
        assertThat(mm.getRam().used(), is(lessThanOrEqualTo(mm.getRam().total())));
    }

    @Test
    public void freeMemoryIsLessThanTotalMemory() throws Exception {
        assertThat(mm.getRam().free(), is(lessThan(mm.getRam().total())));
    }

    @Test
    public void usedSwapIsLessThanOrEqualToTotalSwap() throws Exception {
        assertThat(mm.getSwap().used(), is(lessThanOrEqualTo(mm.getSwap().total())));
    }

    @Test
    public void freeSwapIsLessThanOrEqualToTotalSwap() throws Exception {
        assertThat(mm.getSwap().free(), is(lessThanOrEqualTo(mm.getSwap().total())));
    }

    @Test
    public void ramSizeIsGreaterThanZero() throws Exception {
        assertThat(mm.ramInMB(), is(greaterThan(0L)));
    }
}