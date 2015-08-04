package se.krillsson.maintenance.sigar;

import org.hyperic.sigar.Sigar;
import org.junit.BeforeClass;

import static org.junit.Assume.assumeNoException;

public abstract class CheckSigarLoadsOk {

    @BeforeClass
    public static final void canLoadSigarCheck() {
        try {
            Sigar.load();
        } catch (Throwable e) {
            assumeNoException(e);
        }
    }

}
