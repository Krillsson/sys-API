package com.krillsson.sysapi.util;

import com.krillsson.sysapi.SysAPIApplication;
import org.slf4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class Utils {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Utils.class);


    public static String getVersionFromManifest() throws IOException {
        Class clazz = SysAPIApplication.class;
        String className = clazz.getSimpleName() + ".class";
        String classPath = clazz.getResource(className).toString();
        if (!classPath.startsWith("jar")) {
            // Class not from JAR
            LOGGER.error("Unable to determine version");
            return "";
        }
        String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) +
                "/META-INF/MANIFEST.MF";
        Manifest manifest = new Manifest(new URL(manifestPath).openStream());
        Attributes attr = manifest.getMainAttributes();
        return "v." + attr.getValue("Version");
    }

    public static double round(double value, int places) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            return 0d;
        }
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public long currentSystemTime() {
        return System.currentTimeMillis();
    }

    public void sleep(long ms) {
        oshi.util.Util.sleep(ms);
    }
}
