package com.krillsson.sysapi.core.metrics.windows

import com.krillsson.sysapi.util.JarLocation
import net.sf.jni4net.Bridge
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class OHMManagerFactory {
    private var monitorManager: DelegatingOHMManager? = null

    fun prerequisitesFilled(): Boolean {
        LOGGER.info("Checking if {} exists: {}", OHM_JNI_WRAPPER_DLL, if (OHM_JNI_WRAPPER_DLL.exists()) "YES" else "NO")
        LOGGER.info(
            "Checking if {} exists: {}",
            OPEN_HARDWARE_MONITOR_LIB_DLL,
            if (OPEN_HARDWARE_MONITOR_LIB_DLL.exists()) "YES" else "NO"
        )
        LOGGER.info(
            "Checking if {} exists: {}",
            OHM_JNI_WRAPPER_J4N_DLL,
            if (OHM_JNI_WRAPPER_J4N_DLL.exists()) "YES" else "NO"
        )
        return OHM_JNI_WRAPPER_DLL.exists() &&
                OPEN_HARDWARE_MONITOR_LIB_DLL.exists() &&
                OHM_JNI_WRAPPER_J4N_DLL.exists()
    }

    fun getMonitorManager(): DelegatingOHMManager {
        checkNotNull(monitorManager) { "MonitorManager requires initialization. Call initialize" }
        return monitorManager!!
    }

    fun initialize(): Boolean {
        LOGGER.info("Enabling OHMJNIWrapper impl. This functionality is EXPERIMENTAL and can be disabled in the configuration.yml (see README.md)")
        Bridge.setDebug(true)
        try {
            Bridge.init(File(JarLocation.LIB_LOCATION))
        } catch (e: Exception) {
            LOGGER.error("Trouble while initializing JNI4Net Bridge. Do I have admin privileges?", e)
            return false
        }

        val factory = loadFromInstallDir()
        if (factory != null) {
            try {
                factory.init()
                this.monitorManager = DelegatingOHMManager(factory.GetManager())
                return true
            } catch (e: Exception) {
                LOGGER.error("Trouble while initializing JNI4Net Bridge. Do I have admin privileges?", e)
                return false
            }
        }
        return false
    }


    private fun loadFromInstallDir(): ohmwrapper.OHMManagerFactory? {
        try {
            LOGGER.debug("Attempting to load {}", OHM_JNI_WRAPPER_DLL)
            Bridge.LoadAndRegisterAssemblyFrom(OHM_JNI_WRAPPER_DLL)
            LOGGER.debug("Attempting to load {}", OHM_JNI_WRAPPER_J4N_DLL)
            Bridge.LoadAndRegisterAssemblyFrom(OHM_JNI_WRAPPER_J4N_DLL)
            LOGGER.debug("Attempting to load {}", OPEN_HARDWARE_MONITOR_LIB_DLL)
            Bridge.LoadAndRegisterAssemblyFrom(OPEN_HARDWARE_MONITOR_LIB_DLL)
            return ohmwrapper.OHMManagerFactory()
        } catch (e: Exception) {
            LOGGER.error("Unable to load OHM from installation directory {}", JarLocation.SOURCE_LIB_LOCATION, e)
            return null
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(OHMManagerFactory::class.java)

        private val OHM_JNI_WRAPPER_DLL = File(JarLocation.LIB_LOCATION + JarLocation.SEPARATOR + "OhmJniWrapper.dll")
        private val OPEN_HARDWARE_MONITOR_LIB_DLL =
            File(JarLocation.LIB_LOCATION + JarLocation.SEPARATOR + "OpenHardwareMonitorLib.dll")
        private val OHM_JNI_WRAPPER_J4N_DLL =
            File(JarLocation.LIB_LOCATION + JarLocation.SEPARATOR + "OhmJniWrapper.j4n.dll")
    }
}
