package se.christianjensen.maintenance.sigar;

import org.hyperic.sigar.Sigar;

public abstract class SigarWrapper {

    protected static Sigar sigar;

    public SigarWrapper(){
        sigar = new Sigar();
    }

    public abstract void initialize();

}
