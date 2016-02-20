package com.krillsson.sysapi.domain.drive;

public enum FileSystemType
{
    // Ordered so that ordinals match the constants
    // in org.hyperic.sigar.FileSystem
    Unknown, None, LocalDisk, Network, Ramdisk, Cdrom, Swap
}
