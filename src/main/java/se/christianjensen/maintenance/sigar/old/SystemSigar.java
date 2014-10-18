package se.christianjensen.maintenance.sigar.old;

import org.hyperic.sigar.OperatingSystem;

public class SystemSigar extends SigarWrapper {

    @Override
    public void initialize() {

    }

    public String osName(){
        return OperatingSystem.NAME_WIN32;
    }


}
