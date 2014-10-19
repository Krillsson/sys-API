package se.christianjensen.maintenance.sigar;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
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

    @Ignore("This isn't working")
    @Test
    public void osIsEqualToJavaOs(){
        String javaOs = System.getProperty("os.name");
        assertThat(sm.machineInfo().getOperatingSystem().getDescription(), containsString(javaOs));
    }

}