package se.krillsson.maintenance.sigar;


import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import se.krillsson.maintenance.representation.system.JvmProperties;
import se.krillsson.maintenance.representation.system.Machine;
import se.krillsson.maintenance.representation.system.OperatingSystem;
import se.krillsson.maintenance.representation.system.UserInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
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

        JvmProperties jvmProperties = getJvmProperties();

        return (new Machine(hostname, users, uptime, OperatingSystem.fromSigarBean(os), jvmProperties));

    }

    private JvmProperties getJvmProperties() {
        Properties p = System.getProperties();
        return new JvmProperties(
                p.getProperty("java.home"),
                p.getProperty("java.class.path"),
                p.getProperty("java.vendor"),
                p.getProperty("java.vendor.url"),
                p.getProperty("java.version"),
                p.getProperty("os.arch"),
                p.getProperty("os.name"),
                p.getProperty("os.version"),
                p.getProperty("user.dir"),
                p.getProperty("user.home"),
                p.getProperty("user.name"));
    }


}
