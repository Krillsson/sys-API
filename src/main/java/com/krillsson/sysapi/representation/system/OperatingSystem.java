package com.krillsson.sysapi.representation.system;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
* Created by christian on 2014-11-30.
*/
public final class OperatingSystem extends SysInfo {

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
