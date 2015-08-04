package com.krillsson.hwapi.sigar;

import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import com.krillsson.hwapi.representation.filesystem.FSType;
import com.krillsson.hwapi.representation.filesystem.FileSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class FilesystemMetrics extends AbstractSigarMetric {

    protected FilesystemMetrics(Sigar sigar) {
        super(sigar);
    }

    public List<FileSystem> getFilesystems() {
        org.hyperic.sigar.FileSystem[] fss = getSigarFilesystems();
        List<FileSystem> result = new ArrayList<>();
        for (org.hyperic.sigar.FileSystem fs : fss) {
            result.add(convertToInternal(fs));
        }
        return result;
   }

    public List<FileSystem> getFileSystemsWithCategory(FSType fsType) {
        org.hyperic.sigar.FileSystem[] fss = getSigarFilesystems();
        return Arrays.asList(fss)
                .stream()
                .filter(f -> f.getType() == fsType.ordinal())
                .map(this::convertToInternal)
                .collect(Collectors.toList());
    }

    public FileSystem getFileSystemById(String name) {
        org.hyperic.sigar.FileSystem[] fss = getSigarFilesystems();
        FileSystem fileSystem;

        try {
            fileSystem = Arrays.asList(fss)
                    .stream()
                    .filter(f -> f.getDevName().replace(":", "").replace("\\", "").equals(name))
                    .map(this::convertToInternal)
                    .findFirst()
                    .get();
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("No FS named: '" + name + "' were found");
        }
        return fileSystem;
    }

    private FileSystem convertToInternal(org.hyperic.sigar.FileSystem fs) {
        long totalSizeKB = 0L;
        long freeSpaceKB = 0L;
        try {
            FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
            totalSizeKB = usage.getTotal();
            freeSpaceKB = usage.getFree();
        } catch (SigarException e) {
            //Swallow this exception
        }
        return FileSystem.fromSigarBean(fs, totalSizeKB, freeSpaceKB);
    }

    private org.hyperic.sigar.FileSystem[] getSigarFilesystems() {
        org.hyperic.sigar.FileSystem[] fss;

        try {
            fss = sigar.getFileSystemList();
        } catch (SigarException e) {
            // TODO: decide what message to put here
            throw new IllegalArgumentException(e.getMessage());
        }

        if (fss == null) {
            // TODO: decide what message to put here
            throw new IllegalArgumentException("Internal derp");
        }

        return fss;
    }


}
