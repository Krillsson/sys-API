package se.christianjensen.maintenance.metrics;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.OperatingSystem;

public class SigarWrapper {

    public String CpuModel() {
        CpuInfo cpuInfo = new CpuInfo();
        return cpuInfo.getModel();
    }

    public String osName() {
        return OperatingSystem.NAME_WIN32;
    }



}
