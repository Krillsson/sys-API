package se.christianjensen.maintenance.representation.system;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
* Created by christian on 2014-11-30.
*/
public class SysInfo {
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

    public SysInfo(String name,
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
