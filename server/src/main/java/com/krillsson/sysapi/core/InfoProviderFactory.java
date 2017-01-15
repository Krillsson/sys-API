package com.krillsson.sysapi.core;

import com.krillsson.sysapi.config.SystemApiConfiguration;
import com.krillsson.sysapi.core.windows.WindowsInfoProvider;
import oshi.PlatformEnum;

public class InfoProviderFactory {
    private final PlatformEnum os;
    private final SystemApiConfiguration configuration;

    public InfoProviderFactory(PlatformEnum os, SystemApiConfiguration configuration) {
        this.os = os;
        this.configuration = configuration;
    }

    public InfoProvider provide() {
        switch (os) {
            case WINDOWS:
                if (configuration.windows() == null || configuration.windows().enableOhmJniWrapper()) {
                    WindowsInfoProvider windowsInfoProvider = new WindowsInfoProvider();
                    if (windowsInfoProvider.canProvide()) {
                        return windowsInfoProvider;
                    }
                }
            case LINUX:
            case MACOSX:
                //https://github.com/Chris911/iStats
            case FREEBSD:
            case SOLARIS:
            case UNKNOWN:
            default:
                return new DefaultInfoProvider();

        }
    }

}
