package com.krillsson.sysapi.sigar;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class NetworkMetricsTest extends CheckSigarLoadsOk {
    NetworkMetrics nm;

    @Before
    public void setUp() {
        nm = SigarMetrics.getInstance().network();
    }

    @Test
    public void networkConfigsIsGreaterThanZero() throws Exception {
        assertThat(nm.getConfigs().size(), is(greaterThan(0)));
    }

    @Test
    public void jvmAndSigarIsReportingTheSameDomainName() throws Exception {
        String fullHostName = java.net.InetAddress.getLocalHost().getCanonicalHostName();
        assertThat(fullHostName, containsString(nm.getNetworkInfo().getDomainName()));
    }

    @Test
    public void queryingForExistingConfigShouldWork() throws Exception {
        nm.getConfigById(nm.getConfigs().get(0).getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void queryingForNonExistenConfigShouldThrowException() throws Exception {
        String nonExistentConfig = "derp";

        nm.getConfigById(nonExistentConfig);
    }
}
