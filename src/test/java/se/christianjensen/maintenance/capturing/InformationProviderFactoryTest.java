package se.christianjensen.maintenance.capturing;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InformationProviderFactoryTest {
    InformationProviderFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new InformationProviderFactory();

    }

    @Test
    public void testGetOs() throws Exception {
        //There's no reliable way to test this. My buildserver runs windows and my devmachine runs windows. Sorry! :)
        assertEquals(factory.getOs(), InformationProviderFactory.OsType.WINDOWS);
    }
}