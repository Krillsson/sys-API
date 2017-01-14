package com.krillsson.sysapi.ohm;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.domain.Thermals;
import com.krillsson.sysapi.domain.gpu.Gpu;
import com.krillsson.sysapi.domain.motherboard.Motherboard;
import com.krillsson.sysapi.extension.windows.WindowsInfoProvider;
import com.krillsson.sysapi.oshi.DisplaysResource;
import com.krillsson.sysapi.util.OperatingSystem;
import com.krillsson.sysapi.util.TemperatureUtils;
import io.dropwizard.auth.Auth;
import org.jutils.jhardware.info.HardwareFactory;
import org.jutils.jhardware.info.InfoType;
import org.jutils.jhardware.model.GraphicsCardInfo;
import oshi.json.hardware.Display;

import java.util.List;

public class OhmDisplayResource extends DisplaysResource {

    private final WindowsInfoProvider windowsInfoProvider;

    public OhmDisplayResource(Display[] displays, WindowsInfoProvider windowsInfoProvider) {
        super(displays);
        this.windowsInfoProvider = windowsInfoProvider;
    }

    @Override
    public Object getRoot(@Auth UserConfiguration user) {
        Object root = super.getRoot(user);
        List<Gpu> gpuMonitors = windowsInfoProvider.gpus();
        Motherboard motherboard = windowsInfoProvider.motherboard();

        return new Object() {
            public Display[] getDisplays() {
                return (Display[]) root;
            }

            public GraphicsCardInfo getGraphicsCardInfo() {
                return (GraphicsCardInfo) HardwareFactory.get(InfoType.GRAPHICSCARD).getInfo();
            }

            public List<Gpu> getGpuMonitors() {
                return gpuMonitors;
            }

            public Thermals getTemperatures() {
                return new TemperatureUtils(OperatingSystem.WINDOWS).getCpuThermals();
            }

            public Motherboard getMotherboard() {
                return motherboard;
            }
        };
    }
}
