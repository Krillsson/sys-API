package se.krillsson.maintenance.sigar;

import org.hyperic.sigar.Sigar;

public abstract class AbstractSigarMetric {
    protected final Sigar sigar;

    protected AbstractSigarMetric(Sigar sigar) {
        this.sigar = sigar;
    }
}
