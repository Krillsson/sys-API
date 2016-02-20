package com.krillsson.sysapi.provider;

import com.krillsson.sysapi.ohm.WindowsInfoProvider;
import com.krillsson.sysapi.util.OperatingSystem;
import org.junit.Test;

import static org.junit.Assert.*;

public class InfoProviderFactoryTest
{

    @Test
    public void testWindowsMachineShouldGiveWindowsProvider() throws Exception
    {
        final InfoProviderFactory factory = InfoProviderFactory.initialize(OperatingSystem.WINDOWS);
        final InfoProvider infoProvider = factory.getInfoProvider();
        assertTrue(infoProvider instanceof WindowsInfoProvider);
    }

    @Test
    public void testMacMachineShouldGiveDefaultProvider() throws Exception
    {
        final InfoProviderFactory factory = InfoProviderFactory.initialize(OperatingSystem.MAC_OS);
        final InfoProvider infoProvider = factory.getInfoProvider();
        assertTrue(infoProvider instanceof DefaultInfoProvider);
    }
}