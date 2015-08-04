package com.krillsson.sysapi.sigar;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class SystemSigarTest extends CheckSigarLoadsOk{
    private SystemSigar sm;

    @Before
    public void setUp() {
        sm = SigarKeeper.getInstance().system();
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