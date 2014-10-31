package se.christianjensen.maintenance.sigar;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class SystemMetrics extends AbstractSigarMetric {
    protected SystemMetrics(Sigar sigar) {
        super(sigar);
    }

    public static class SysInfo {
        private final String name;
        private final String version;
        private final String arch;
        private final String machine;
        private final String description;
        private final String patchLevel;
        private final String vendor;
        private final String vendorVersion;
        private final String vendorName;
        private final String vendorCodeName;

        private SysInfo(String name,
                        String version, String arch,
                        String machine, String description,
                        String patchLevel, String vendor,
                        String vendorVersion, String vendorName,
                        String vendorCodeName
        ) {
            this.name = name;
            this.version = version;
            this.arch = arch;
            this.machine = machine;
            this.description = description;
            this.patchLevel = patchLevel;
            this.vendor = vendor;
            this.vendorVersion = vendorVersion;
            this.vendorName = vendorName;
            this.vendorCodeName = vendorCodeName;

        }


        @JsonProperty
        public String getName() {
            return name;
        }

        @JsonProperty
        public String getVersion() {
            return version;
        }

        @JsonProperty
        public String getArch() {
            return arch;
        }

        @JsonProperty
        public String getMachine() {
            return machine;
        }

        @JsonProperty

        public String getDescription() {
            return description;
        }

        @JsonProperty
        public String getPatchLevel() {
            return patchLevel;
        }

        @JsonProperty
        public String getVendor() {
            return vendor;
        }

        @JsonProperty
        public String getVendorVersion() {
            return vendorVersion;
        }

        @JsonProperty
        public String getVendorName() {
            return vendorName;
        }

        @JsonProperty
        public String getVendorCodeName() {
            return vendorCodeName;
        }


    }

    public static final class OperatingSystem extends SysInfo {

        private String dataModel;
        private String cpuEndian;

        public OperatingSystem(String name, String version, String arch, String machine, String description, String patchLevel, String vendor, String vendorVersion, String vendorName, String vendorCodeName, String dataModel, String cpuEndian) {
            super(name, version, arch, machine, description, patchLevel, vendor, vendorVersion, vendorName, vendorCodeName);
            this.dataModel = dataModel;
            this.cpuEndian = cpuEndian;
        }

        public static OperatingSystem fromSigarBean(org.hyperic.sigar.OperatingSystem os) {
            return new OperatingSystem(os.getName(),
                    os.getVersion(),
                    os.getArch(),
                    os.getMachine(),
                    os.getDescription(),
                    os.getPatchLevel(),
                    os.getVendor(),
                    os.getVendorVersion(),
                    os.getVendorName(),
                    os.getVendorCodeName(),
                    os.getCpuEndian(),
                    os.getDataModel()
            );
        }

        @JsonProperty
        public String getDataModel() {
            return dataModel;
        }

        @JsonProperty
        public String getCpuEndian() {
            return cpuEndian;
        }
    }

    public static class Machine {
        private String hostname;
        private double uptime;
        private OperatingSystem operatingSystem;

        public Machine(String hostname, double uptime, OperatingSystem operatingSystem) {
            this.hostname = hostname;
            this.operatingSystem = operatingSystem;
            this.uptime = uptime;
        }

        @JsonProperty
        public String getHostname() {
            return hostname;
        }

        @JsonProperty
        public double getUptime() {
            return uptime;
        }

        @JsonProperty
        public OperatingSystem getOperatingSystem() {
            return operatingSystem;
        }


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
