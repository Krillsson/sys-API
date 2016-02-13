package com.krillsson.sysapi.provider;

import com.krillsson.sysapi.util.OperatingSystem;

public class InfoProviderFactory
{
    private InfoProvider infoProvider;

    private InfoProviderFactory(InfoProvider infoProvider){
        this.infoProvider = infoProvider;
        //hidden
    }

    public static InfoProviderFactory initialize(OperatingSystem os)
    {
        switch (os)
        {
            case WINDOWS:
                WindowsInfoProvider provider = new WindowsInfoProvider();
                return new InfoProviderFactory(provider);
            case LINUX:
            case MAC_OS:
            case FREE_BSD:
            case UNKNOWN:
            default:
                DefaultInfoProvider defaultInfoProvider = new DefaultInfoProvider();
                return new InfoProviderFactory(defaultInfoProvider);
        }
    }
    public InfoProvider getInfoProvider()
    {
        return infoProvider;
    }
}
