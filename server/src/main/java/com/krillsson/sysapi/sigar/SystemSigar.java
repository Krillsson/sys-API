package com.krillsson.sysapi.sigar;


import com.krillsson.sysapi.domain.network.NetworkInterfaceConfig;
import com.krillsson.sysapi.domain.system.*;
import com.krillsson.sysapi.domain.system.System;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class SystemSigar extends SigarWrapper {

    private SigarKeeper sigarKeeper;
    private final JvmProperties jvmProperties;

    protected SystemSigar(Sigar sigar, SigarKeeper sigarKeeper) {
        super(sigar);
        this.sigarKeeper = sigarKeeper;
        this.jvmProperties = getJvmProperties();
    }

    public double getUptime() {
        try {
            return sigar.getUptime().getUptime();
        } catch (SigarException e) {
            return 0.0;
        }
    }

    public OperatingSystem getOperatingSystem() {
        return SigarBeanConverter.fromSigarBean(org.hyperic.sigar.OperatingSystem.getInstance());
    }

    public System getSystem() {
        try {
            return new System(sigar.getNetInfo().getHostName(),
                    sigar.getUptime().getUptime(),
                    jvmProperties.getOsName(),
                    jvmProperties.getOsVersion(),
                    sigarKeeper.cpu().totalCpuTime(),
                    sigarKeeper.memory().getRam(),
                    sigarKeeper.process().getStatistics());
        } catch (SigarException e) {
            // give up
            return null;
        }
    }

    public System getExtendedSystem(String filesystemId, String nicId) {
        try {
            NetworkInterfaceConfig configById = sigarKeeper.network().getConfigById(nicId);
            configById.setNetworkInterfaceSpeed(sigarKeeper.network().getSpeed(nicId));
            return new System(
                    sigar.getNetInfo().getHostName(),
                    sigar.getUptime().getUptime(),
                    jvmProperties.getOsName(),
                    jvmProperties.getOsVersion(),
                    sigarKeeper.cpu().totalCpuTime(),
                    sigarKeeper.memory().getRam(),
                    sigarKeeper.process().getStatistics(),
                    sigarKeeper.filesystems().getFileSystemById(filesystemId),
                    configById
            );
        } catch (SigarException e) {
            // give up
            return null;
        }
    }

    public List<UserInfo> getUsers() {
        List<UserInfo> users;
        try {
            List<org.hyperic.sigar.Who> who = Arrays.asList(sigar.getWhoList());
            users = who.stream().map(SigarBeanConverter::fromSigarBean).collect(Collectors.toList()); //Stream magic
            return users;
        } catch (SigarException e) {
            // give up
            return null;
        }
    }

    public JvmProperties getJvmProperties() {
        Properties p = java.lang.System.getProperties();
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
