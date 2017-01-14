package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.windows.WindowsInfoProvider;
import com.krillsson.sysapi.util.OperatingSystem;

public class InfoProviderFactory
{
    private InfoProviderFactory(){
        //hidden
    }

    public static InfoProvider provide(OperatingSystem os)
    {
        switch (os)
        {
            case WINDOWS:
                WindowsInfoProvider windowsInfoProvider = new WindowsInfoProvider();
                if(windowsInfoProvider.canProvide()){
                    return windowsInfoProvider;
                }
            case LINUX:
            case MAC_OS:
                //https://github.com/Chris911/iStats
            case FREE_BSD:
            case UNKNOWN:
            default:
                return new DefaultInfoProvider();

        }
    }

}
