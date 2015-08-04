package se.krillsson.maintenance.sigar;

import org.junit.Test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

public class SigarMetricsTest extends CheckSigarLoadsOk {

    @Test
    public void pidIsGreaterThanZero() throws Exception {
        assertThat(SigarMetrics.getInstance().pid(), is(greaterThan(0L)));
    }
}