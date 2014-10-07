package se.christianjensen.maintenance.sigar;

import org.hyperic.sigar.OperatingSystem;

/**
 * Created by christian on 2014-10-07.
 */
public class SystemSigar extends SigarWrapper {

    @Override
    public void initialize() {

    }

    public String osName(){
        return OperatingSystem.NAME_WIN32;
    }


}
