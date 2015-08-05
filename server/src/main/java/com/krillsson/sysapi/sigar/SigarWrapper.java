package com.krillsson.sysapi.sigar;

import org.hyperic.sigar.Sigar;

public abstract class SigarWrapper {
    protected final Sigar sigar;

    protected SigarWrapper(Sigar sigar) {
        this.sigar = sigar;
    }
}
