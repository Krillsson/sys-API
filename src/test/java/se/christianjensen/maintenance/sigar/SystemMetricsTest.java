package se.christianjensen.maintenance.sigar;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.greaterThan;

import static org.junit.Assert.*;

public class SystemMetricsTest extends CheckSigarLoadsOk{
    private SystemMetrics sm;

    @Before
    public void setUp() {
        sm = SigarMetrics.getInstance().system();
    }

    @Test
    public void uptimeIsGreaterThanZero(){
        assertThat(sm.machineInfo().getUptime(), is(greaterThan(0.0)));
    }

}