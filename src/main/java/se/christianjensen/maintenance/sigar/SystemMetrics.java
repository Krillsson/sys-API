package se.christianjensen.maintenance.sigar;


import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import se.christianjensen.maintenance.representation.system.Machine;
import se.christianjensen.maintenance.representation.system.OperatingSystem;
import se.christianjensen.maintenance.representation.system.UserInfo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SystemMetrics extends AbstractSigarMetric {
    protected SystemMetrics(Sigar sigar) {
        super(sigar);
    }

    public Machine machineInfo() {
        double uptime = 0.0;
        org.hyperic.sigar.OperatingSystem os;
        List<UserInfo> users;
        String hostname = "";
        try {
            os = org.hyperic.sigar.OperatingSystem.getInstance();
            uptime = sigar.getUptime().getUptime();
            List<org.hyperic.sigar.Who> who = Arrays.asList(sigar.getWhoList());
            users = who.stream().map(UserInfo::fromSigarBean).collect(Collectors.toList()); //Stream magic
            hostname = sigar.getNetInfo().getHostName();
        } catch (SigarException e) {
            // give up
            return null;
        }


        return (new Machine(hostname, users, uptime, OperatingSystem.fromSigarBean(os)));

    }


}
