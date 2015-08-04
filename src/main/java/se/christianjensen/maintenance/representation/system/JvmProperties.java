package se.christianjensen.maintenance.representation.system;


import com.fasterxml.jackson.annotation.JsonProperty;

public class JvmProperties {
    private String home, classPath, vendor,
            vendorUrl, version, osArch, osName,
            osVersion, userDir, userHome, username;

    public JvmProperties(String home, String classPath, String vendor, String vendorUrl, String version, String osArch, String osName, String osVersion, String userDir, String userHome, String username) {
        this.home = home;
        this.classPath = classPath;
        this.vendor = vendor;
        this.vendorUrl = vendorUrl;
        this.version = version;
        this.osArch = osArch;
        this.osName = osName;
        this.osVersion = osVersion;
        this.userDir = userDir;
        this.userHome = userHome;
        this.username = username;
    }

    @JsonProperty
    public String getHome() {
        return home;
    }

    @JsonProperty
    public String getClassPath() {
        return classPath;
    }

    @JsonProperty
    public String getVendor() {
        return vendor;
    }

    @JsonProperty
    public String getVendorUrl() {
        return vendorUrl;
    }

    @JsonProperty
    public String getVersion() {
        return version;
    }

    @JsonProperty
    public String getOsArch() {
        return osArch;
    }

    @JsonProperty
    public String getOsName() {
        return osName;
    }

    @JsonProperty
    public String getOsVersion() {
        return osVersion;
    }

    @JsonProperty
    public String getUserDir() {
        return userDir;
    }

    @JsonProperty
    public String getUserHome() {
        return userHome;
    }

    @JsonProperty
    public String getUsername() {
        return username;
    }
}
