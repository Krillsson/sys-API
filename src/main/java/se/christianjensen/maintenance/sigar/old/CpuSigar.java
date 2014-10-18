package se.christianjensen.maintenance.sigar.old;


import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.SigarException;

public class CpuSigar extends SigarWrapper {

    CpuInfo[] cpuInfo;

    @Override
    public void initialize() {
        try {
            cpuInfo = sigar.getCpuInfoList();
        } catch (SigarException e) {
            e.printStackTrace();
        }
    }

    public CpuInfo[] getCpuInfo() {
        return cpuInfo;
    }
}
