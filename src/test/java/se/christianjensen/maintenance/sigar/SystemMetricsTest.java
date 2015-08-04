package se.christianjensen.maintenance.sigar;

import org.junit.Before;
import org.junit.Test;
import se.christianjensen.maintenance.representation.system.UserInfo;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

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

    @Test
    public void hostNameIsEqualToWhatJvmThinks() throws Exception {
        String javaHostname =  java.net.InetAddress.getLocalHost().getHostName();
        assertThat(sm.machineInfo().getHostname(), containsString(javaHostname));
    }
}