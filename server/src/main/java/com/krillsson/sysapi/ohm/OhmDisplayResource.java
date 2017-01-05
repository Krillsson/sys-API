package com.krillsson.sysapi.ohm;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.oshi.DisplaysResource;
import io.dropwizard.auth.Auth;
import ohmwrapper.GpuMonitor;
import oshi.hardware.Display;

public class OhmDisplayResource extends DisplaysResource {

    private final WindowsInfoProvider windowsInfoProvider;

    public OhmDisplayResource(Display[] displays, WindowsInfoProvider windowsInfoProvider) {
        super(displays);
        this.windowsInfoProvider = windowsInfoProvider;
    }

    @Override
    public Object getRoot(@Auth UserConfiguration user) {
        Object root = super.getRoot(user);
        GpuMonitor[] gpuMonitors = windowsInfoProvider.ohmGpu();

        return new Object(){
            public Display[] getDisplays(){
                return (Display[]) root;
            }
            public GpuMonitor[] getGpuMonitors(){
                return gpuMonitors;
            }
        };
    }
}
