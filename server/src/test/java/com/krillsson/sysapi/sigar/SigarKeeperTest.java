package com.krillsson.sysapi.sigar;

import org.junit.Test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

public class SigarKeeperTest extends CheckSigarLoadsOk {

    @Test
    public void pidIsGreaterThanZero() throws Exception {
        assertThat(SigarKeeper.getInstance().pid(), is(greaterThan(0L)));
    }
}