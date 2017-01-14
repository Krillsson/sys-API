package com.krillsson.sysapi.extension;

import com.krillsson.sysapi.extension.windows.WindowsInfoProvider;
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
            case FREE_BSD:
            case UNKNOWN:
            default:
                return new DefaultInfoProvider();

        }
    }

}
