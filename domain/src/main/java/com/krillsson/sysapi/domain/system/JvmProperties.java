package com.krillsson.sysapi.domain.system;

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

    public String getHome() {
        return home;
    }

    public String getClassPath() {
        return classPath;
    }

    public String getVendor() {
        return vendor;
    }

    public String getVendorUrl() {
        return vendorUrl;
    }

    public String getVersion() {
        return version;
    }

    public String getOsArch() {
        return osArch;
    }

    public String getOsName() {
        return osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getUserDir() {
        return userDir;
    }

    public String getUserHome() {
        return userHome;
    }

    public String getUsername() {
        return username;
    }
}
