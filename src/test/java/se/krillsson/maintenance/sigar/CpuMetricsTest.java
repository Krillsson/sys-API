package se.krillsson.maintenance.sigar;

import org.junit.Before;
import org.junit.Test;
import se.krillsson.maintenance.representation.cpu.CpuTime;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class CpuMetricsTest {

    private CpuMetrics cm;

    @Before
    public void setUp() {
        cm = SigarMetrics.getInstance().cpu();
    }

    @Test
    public void cpuCoreCountIsGreaterThanZero() throws Exception {
        assertThat(cm.getCpu().getCpuInfo().getTotalCores(), is(greaterThan(0)));
    }

    @Test
    public void lengthOfCpuListMatchesCoreCount() throws Exception {
        assertThat(cm.cpuTimesPerCore(cm.cpuPercList()).size(), is(equalTo(cm.getCpu().getCpuInfo().getTotalCores())));
    }

    @Test
    public void cpuTimesAddUpToApproximatelyOne() throws Exception {
        CpuTime t = cm.cpuTimesPerCore(cm.cpuPercList()).get(0);
        assertThat(t.user() + t.sys() + t.nice() + t.waiting() + t.idle() + t.irq(),
                is(closeTo(1.0, 0.05)));
    }

    @Test
    public void gettingCpuTimeByExistingCore() throws Exception {
        cm.getCpuTimeByCoreIndex(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void gettingCpuTimeByNonExistentCore() throws Exception {
        cm.getCpuTimeByCoreIndex(100);
    }
}