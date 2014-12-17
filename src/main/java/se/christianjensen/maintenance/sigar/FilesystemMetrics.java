package se.christianjensen.maintenance.sigar;

import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import se.christianjensen.maintenance.representation.filesystem.FileSystem;

import java.util.ArrayList;
import java.util.List;

public class FilesystemMetrics extends AbstractSigarMetric {

    protected FilesystemMetrics(Sigar sigar) {
        super(sigar);
    }

    public List<FileSystem> filesystems() {
        List<FileSystem> result = new ArrayList<FileSystem>();
        org.hyperic.sigar.FileSystem[] fss = null;
        try {
            fss = sigar.getFileSystemList(); 
        } catch (SigarException e) {
            // give up
            return result;
        }

        if (fss == null) {
            return result;
        }

        for (org.hyperic.sigar.FileSystem fs: fss) {
            long totalSizeKB = 0L;
            long freeSpaceKB = 0L;
            try {
                FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
                totalSizeKB = usage.getTotal();
                freeSpaceKB = usage.getFree();
            } catch (SigarException e) {
                // ignore
            }
            result.add(FileSystem.fromSigarBean(fs, totalSizeKB, freeSpaceKB));
        } 
        return result;
   }


}
