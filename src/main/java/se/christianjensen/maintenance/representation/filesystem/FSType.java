package se.christianjensen.maintenance.representation.filesystem;

/**
* Created by christian on 2014-11-30.
*/
public enum FSType {
    // Ordered so that ordinals match the constants
    // in org.hyperic.sigar.FileSystem
    Unknown, None, LocalDisk, Network, Ramdisk, Cdrom, Swap
}
