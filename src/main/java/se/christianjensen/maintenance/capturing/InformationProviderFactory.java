package se.christianjensen.maintenance.capturing;

import net.sf.jni4net.Bridge;
import openhardwaremonitor.hardware.Computer;
import org.apache.commons.lang.SystemUtils;
import se.christianjensen.maintenance.capturing.Windows.WindowsInformationProvider;
import se.christianjensen.maintenance.sigar.SigarMetrics;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class InformationProviderFactory {
    public enum OsType {
        WINDOWS, MAC, LINUX, UNKNOWN
    }

    public OsType getOs() {
        return os;
    }

    OsType os;

    public InformationProviderFactory() {
        determineOs();
    }

    public InformationProviderInterface getInformationProvider() {

        switch (os) {
            case WINDOWS:
                try {
                    Bridge.setVerbose(true);
                    Bridge.init();
                    Bridge.LoadAndRegisterAssemblyFrom(new File(getClass()
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()
                            .toString()
                            + SystemUtils.FILE_SEPARATOR
                            + "lib"
                            + SystemUtils.FILE_SEPARATOR
                            + "OpenHardwareMonitorLib.j4n.dll"));
                    return new WindowsInformationProvider(SigarMetrics.getInstance(), new Computer());
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
                break;
            case MAC:
                break;
            case LINUX:
                break;
            case UNKNOWN:
                break;
        }
        return null;
    }

    private void determineOs() {
        if (SystemUtils.IS_OS_WINDOWS) {
            os = OsType.WINDOWS;
        } else if (SystemUtils.IS_OS_MAC_OSX) {
            os = OsType.MAC;
        } else if (SystemUtils.IS_OS_LINUX) {
            os = OsType.LINUX;
        } else {
            os = OsType.UNKNOWN;
        }
    }
}
