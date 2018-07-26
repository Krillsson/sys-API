package com.krillsson.sysapi.dto.cpu;

public class CentralProcessor {


    private int logicalProcessorCount;

    private int physicalProcessorCount;

    private long systemUptime;

    private String systemSerialNumber;

    private String name;

    private double systemLoadAverage;

    private String identifier;

    private String family;

    private String vendor;

    private long vendorFreq;

    private String model;

    private String stepping;

    private boolean cpu64bit;

    /**
     * No args constructor for use in serialization
     */
    public CentralProcessor() {
    }

    /**
     * @param model
     * @param vendorFreq
     * @param stepping
     * @param systemLoadAverage
     * @param vendor
     * @param physicalProcessorCount
     * @param logicalProcessorCount
     * @param systemSerialNumber
     * @param cpu64bit
     * @param family
     * @param name
     * @param systemUptime
     * @param identifier
     */
    public CentralProcessor(int logicalProcessorCount, int physicalProcessorCount, long systemUptime, String systemSerialNumber, String name, double systemLoadAverage, String identifier, String family, String vendor, long vendorFreq, String model, String stepping, boolean cpu64bit) {
        super();
        this.logicalProcessorCount = logicalProcessorCount;
        this.physicalProcessorCount = physicalProcessorCount;
        this.systemUptime = systemUptime;
        this.systemSerialNumber = systemSerialNumber;
        this.name = name;
        this.systemLoadAverage = systemLoadAverage;
        this.identifier = identifier;
        this.family = family;
        this.vendor = vendor;
        this.vendorFreq = vendorFreq;
        this.model = model;
        this.stepping = stepping;
        this.cpu64bit = cpu64bit;
    }


    public Integer getLogicalProcessorCount() {
        return logicalProcessorCount;
    }


    public void setLogicalProcessorCount(int logicalProcessorCount) {
        this.logicalProcessorCount = logicalProcessorCount;
    }


    public Integer getPhysicalProcessorCount() {
        return physicalProcessorCount;
    }


    public void setPhysicalProcessorCount(int physicalProcessorCount) {
        this.physicalProcessorCount = physicalProcessorCount;
    }


    public long getSystemUptime() {
        return systemUptime;
    }


    public void setSystemUptime(long systemUptime) {
        this.systemUptime = systemUptime;
    }


    public String getSystemSerialNumber() {
        return systemSerialNumber;
    }


    public void setSystemSerialNumber(String systemSerialNumber) {
        this.systemSerialNumber = systemSerialNumber;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public double getSystemLoadAverage() {
        return systemLoadAverage;
    }


    public void setSystemLoadAverage(double systemLoadAverage) {
        this.systemLoadAverage = systemLoadAverage;
    }


    public String getIdentifier() {
        return identifier;
    }


    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }


    public String getFamily() {
        return family;
    }


    public void setFamily(String family) {
        this.family = family;
    }


    public String getVendor() {
        return vendor;
    }


    public void setVendor(String vendor) {
        this.vendor = vendor;
    }


    public long getVendorFreq() {
        return vendorFreq;
    }


    public void setVendorFreq(long vendorFreq) {
        this.vendorFreq = vendorFreq;
    }


    public String getModel() {
        return model;
    }


    public void setModel(String model) {
        this.model = model;
    }


    public String getStepping() {
        return stepping;
    }


    public void setStepping(String stepping) {
        this.stepping = stepping;
    }


    public boolean getCpu64bit() {
        return cpu64bit;
    }


    public void setCpu64bit(boolean cpu64bit) {
        this.cpu64bit = cpu64bit;
    }

}
