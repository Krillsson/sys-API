package se.christianjensen.maintenance.sigar;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import se.christianjensen.maintenance.representation.system.Machine;
import se.christianjensen.maintenance.representation.system.OperatingSystem;

public class SystemMetrics extends AbstractSigarMetric {
    protected SystemMetrics(Sigar sigar) {
        super(sigar);
    }

    public Machine machineInfo() {
        double uptime = 0.0;
        org.hyperic.sigar.OperatingSystem os;
        String hostname = "";
        try {
            os = org.hyperic.sigar.OperatingSystem.getInstance();
            uptime = sigar.getUptime().getUptime();
            hostname = sigar.getNetInfo().getHostName();
        } catch (SigarException e) {
            // give up
            return null;
        }


        return (new Machine(hostname, uptime, OperatingSystem.fromSigarBean(os)));

    }


}
