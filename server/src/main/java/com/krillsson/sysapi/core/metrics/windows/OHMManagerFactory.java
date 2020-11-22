package com.krillsson.sysapi.core.metrics.windows;

import net.sf.jni4net.Bridge;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;

import static com.krillsson.sysapi.util.JarLocation.*;

public class OHMManagerFactory {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(OHMManagerFactory.class);

    private static final File OHM_JNI_WRAPPER_DLL = new File(LIB_LOCATION + SEPARATOR + "OhmJniWrapper.dll");
    private static final File OPEN_HARDWARE_MONITOR_LIB_DLL = new File(LIB_LOCATION + SEPARATOR + "OpenHardwareMonitorLib.dll");
    private static final File OHM_JNI_WRAPPER_J4N_DLL = new File(LIB_LOCATION + SEPARATOR + "OhmJniWrapper.j4n.dll");

    private DelegatingOHMManager monitorManager;

    public boolean prerequisitesFilled() {
        LOGGER.info("Checking if {} exists: {}", OHM_JNI_WRAPPER_DLL, OHM_JNI_WRAPPER_DLL.exists() ? "YES" : "NO");
        LOGGER.info("Checking if {} exists: {}", OPEN_HARDWARE_MONITOR_LIB_DLL, OPEN_HARDWARE_MONITOR_LIB_DLL.exists() ? "YES" : "NO");
        LOGGER.info("Checking if {} exists: {}", OHM_JNI_WRAPPER_J4N_DLL, OHM_JNI_WRAPPER_J4N_DLL.exists() ? "YES" : "NO");
        return OHM_JNI_WRAPPER_DLL.exists() &&
                OPEN_HARDWARE_MONITOR_LIB_DLL.exists() &&
                OHM_JNI_WRAPPER_J4N_DLL.exists();
    }

    public DelegatingOHMManager getMonitorManager() {
        if (monitorManager == null) {
            throw new IllegalStateException("MonitorManager requires initialization. Call initialize");
        }
        return monitorManager;
    }

    public boolean initialize() {
        LOGGER.info("Enabling OHMJNIWrapper impl. This functionality is EXPERIMENTAL and can be disabled in the configuration.yml (see README.md)");
        Bridge.setDebug(true);
        try {
            Bridge.init(new File(LIB_LOCATION));
        } catch (Exception e) {
            LOGGER.error("Trouble while initializing JNI4Net Bridge. Do I have admin privileges?", e);
            return false;
        }

        ohmwrapper.OHMManagerFactory factory = loadFromInstallDir();
        if (factory != null) {
            try {
                factory.init();
                this.monitorManager = new DelegatingOHMManager(factory.GetManager());
                return true;
            } catch (Exception e) {
                LOGGER.error("Trouble while initializing JNI4Net Bridge. Do I have admin privileges?", e);
                return false;
            }
        }
        return false;
    }


    private ohmwrapper.OHMManagerFactory loadFromInstallDir() {
        try {
            LOGGER.debug("Attempting to load {}", OHM_JNI_WRAPPER_DLL);
            Bridge.LoadAndRegisterAssemblyFrom(OHM_JNI_WRAPPER_DLL);
            LOGGER.debug("Attempting to load {}", OHM_JNI_WRAPPER_J4N_DLL);
            Bridge.LoadAndRegisterAssemblyFrom(OHM_JNI_WRAPPER_J4N_DLL);
            LOGGER.debug("Attempting to load {}", OPEN_HARDWARE_MONITOR_LIB_DLL);
            Bridge.LoadAndRegisterAssemblyFrom(OPEN_HARDWARE_MONITOR_LIB_DLL);
            return new ohmwrapper.OHMManagerFactory();
        } catch (Exception e) {
            LOGGER.error("Unable to load OHM from installation directory {}", SOURCE_LIB_LOCATION, e);
            return null;
        }

    }
}
