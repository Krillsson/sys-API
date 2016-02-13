package com.krillsson.sysapi.provider;

import com.krillsson.sysapi.domain.cpu.Cpu;
import com.krillsson.sysapi.util.NullSafeOhmMonitor;
import net.sf.jni4net.Bridge;
import ohmwrapper.CpuMonitor;
import ohmwrapper.MonitorManager;
import ohmwrapper.OHMManagerFactory;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

import static com.krillsson.sysapi.util.NullSafeOhmMonitor.*;

public class WindowsInfoProvider extends DefaultInfoProvider
{
    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WindowsInfoProvider.class.getSimpleName());

    MonitorManager monitorManager;

    protected WindowsInfoProvider()
    {
        try
        {
            initBridge();
        }
        catch (Exception e)
        {
            LOGGER.error("Unable to initialize JNI4Net Bridge. Do I have admin privileges?", e);
        }
    }

    @Override
    public Cpu cpu()
    {
        Cpu cpu = super.cpu();
        if(monitorManager.CpuMonitors().length > 0){
            CpuMonitor cpuMonitor = monitorManager.CpuMonitors()[0];
            cpu.setFanPercent(nullSafe(cpuMonitor.getFanPercent()).getValue());
            cpu.setFanRpm(nullSafe(cpuMonitor.getFanRPM()).getValue());
            cpu.setTemperature(nullSafe(cpuMonitor.getPackageTemperature()).getValue());
        }
        return cpu;
    }

    private void initBridge() throws IOException
    {
        Bridge.setVerbose(true);
        Bridge.init();
        File ohmJniWrapperDll;
        File ohmJniWrapperJ4nDll;
        File openHardwareMonitorLibDll;
        if (new File("server/lib/OhmJniWrapper.dll").exists())
        {
            //For testing
            ohmJniWrapperDll = new File("server/lib/OhmJniWrapper.dll");
            ohmJniWrapperJ4nDll = new File("server/lib/OhmJniWrapper.j4n.dll");
            openHardwareMonitorLibDll = new File("server/lib/OpenHardwareMonitorLib.dll");
        }
        else
        {
            //For deployment
            ohmJniWrapperDll = new File("lib/OhmJniWrapper.dll");
            ohmJniWrapperJ4nDll = new File("lib/OhmJniWrapper.j4n.dll");
            openHardwareMonitorLibDll = new File("lib/OpenHardwareMonitorLib.dll");
        }

        Bridge.LoadAndRegisterAssemblyFrom(ohmJniWrapperDll);
        Bridge.LoadAndRegisterAssemblyFrom(ohmJniWrapperJ4nDll);
        Bridge.LoadAndRegisterAssemblyFrom(openHardwareMonitorLibDll);
        OHMManagerFactory factory = new OHMManagerFactory();
        factory.init();
        this.monitorManager = factory.GetManager();
    }
}
