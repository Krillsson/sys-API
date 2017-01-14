package com.krillsson.sysapi.util;

import com.krillsson.sysapi.MaintenanceApplication;

import java.io.File;
import java.net.URISyntaxException;


public class JarLocation {
    private static final File JAR = jarLocation();
    private static final boolean IS_JAR = JAR.isFile() && JAR.getName().endsWith(".jar");
    public static final String SEPARATOR = System.getProperty("file.separator");
    public static final String SOURCE_LIB_LOCATION = JAR.getParentFile().getParent() + SEPARATOR + "lib" + SEPARATOR;
    private static final String JAR_LIB_LOCATION = JAR.getParentFile().getParent() + SEPARATOR + "lib" + SEPARATOR;
    public static final String LIB_LOCATION = IS_JAR ? JAR_LIB_LOCATION : SOURCE_LIB_LOCATION;

    private static File jarLocation() {
        try {
            return new File(MaintenanceApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            return new File(MaintenanceApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        }
    }
}
