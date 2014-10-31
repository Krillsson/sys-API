package se.christianjensen.maintenance.sigar;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NetworkMetricsTest extends CheckSigarLoadsOk{
    NetworkMetrics nm;

    @Before
    public void setUp() {
        nm = SigarMetrics.getInstance().network();
    }

    @Test
    public void testGetConfigs() throws Exception {

    }

    @Test
    public void testGetNetworkInfo() throws Exception {
        
    }
}