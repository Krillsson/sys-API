package com.krillsson.sysapi.sigar;


import org.junit.Before;
import org.junit.Test;
import com.krillsson.sysapi.domain.drive.Drive;

import java.io.File;
import java.util.List;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FilesystemSigarTest extends CheckSigarLoadsOk {
    private final double MARGIN_BYTES = 1024 * 1024 * 50; // 50MB

    private FilesystemSigar fsm;

    @Before
    public void setUp() {
        fsm = SigarKeeper.getInstance().filesystems();
    }

    @Test
    public void usageNumbersApproximatelyMatchThoseReturnedByJavaFile() throws Exception {
        File[] roots = File.listRoots();
        List<Drive> fss = fsm.getFilesystems();
        for (File root: roots) {
            for (Drive fs: fss) {
                if (fs.getUsage() != null && new File(fs.mountPoint()).equals(root)) {
                    System.out.println("Testing drive mounted at " + fs.mountPoint());
                    assertThat((double) (root.getTotalSpace()), //
                            is(closeTo((double) (fs.getUsage().getTotalSizeKB() * 1024), MARGIN_BYTES)));
                    assertThat((double) (root.getFreeSpace()), //
                            is(closeTo((double) (fs.getUsage().getFreeSpaceKB() * 1024), MARGIN_BYTES)));
                }
            }
        }
    }

}