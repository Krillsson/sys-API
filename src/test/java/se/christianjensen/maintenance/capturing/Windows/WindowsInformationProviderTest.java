package se.christianjensen.maintenance.capturing.Windows;

import net.sf.jni4net.Bridge;
import openhardwaremonitor.hardware.Computer;
import org.junit.Before;
import org.junit.Test;
import se.christianjensen.maintenance.sigar.SigarMetrics;

import java.io.File;
import java.io.IOException;

public class WindowsInformationProviderTest {

    private WindowsInformationProvider windowsInformationProvider;

    @Before
    public void setUp() throws Exception {
        try {
            Bridge.setVerbose(true);
            Bridge.init();
            Bridge.LoadAndRegisterAssemblyFrom(new File("C:/Users/christian/IdeaProjects/maintenance-api/lib/OpenHardwareMonitorLib.dll"));
            windowsInformationProvider = new WindowsInformationProvider(SigarMetrics.getInstance(), new Computer());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCpus() throws Exception {
        windowsInformationProvider.getCpus();
    }
}