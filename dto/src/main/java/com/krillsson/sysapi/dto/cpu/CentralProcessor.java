package com.krillsson.sysapi.dto.cpu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "logicalProcessorCount",
        "physicalProcessorCount",
        "systemUptime",
        "processorCpuLoadTicks",
        "systemSerialNumber",
        "systemCpuLoadTicks",
        "name",
        "systemLoadAverage",
        "systemCpuLoad",
        "identifier",
        "family",
        "vendor",
        "vendorFreq",
        "model",
        "stepping",
        "cpu64bit",
        "processorCpuLoadBetweenTicks",
        "systemCpuLoadBetweenTicks"
})
public class CentralProcessor {

    @JsonProperty("logicalProcessorCount")
    private int logicalProcessorCount;
    @JsonProperty("physicalProcessorCount")
    private int physicalProcessorCount;
    @JsonProperty("systemUptime")
    private long systemUptime;
    @JsonProperty("processorCpuLoadTicks")
    private long[][] processorCpuLoadTicks = null;
    @JsonProperty("systemSerialNumber")
    private String systemSerialNumber;
    @JsonProperty("systemCpuLoadTicks")
    private long[] systemCpuLoadTicks = null;
    @JsonProperty("name")
    private String name;
    @JsonProperty("systemLoadAverage")
    private double systemLoadAverage;
    @JsonProperty("systemCpuLoad")
    private double systemCpuLoad;
    @JsonProperty("identifier")
    private String identifier;
    @JsonProperty("family")
    private String family;
    @JsonProperty("vendor")
    private String vendor;
    @JsonProperty("vendorFreq")
    private long vendorFreq;
    @JsonProperty("model")
    private String model;
    @JsonProperty("stepping")
    private String stepping;
    @JsonProperty("cpu64bit")
    private boolean cpu64bit;
    @JsonProperty("processorCpuLoadBetweenTicks")
    private double[] processorCpuLoadBetweenTicks = null;
    @JsonProperty("systemCpuLoadBetweenTicks")
    private double systemCpuLoadBetweenTicks;

    /**
     * No args constructor for use in serialization
     */
    public CentralProcessor() {
    }

    /**
     * @param processorCpuLoadTicks
     * @param model
     * @param vendorFreq
     * @param stepping
     * @param systemLoadAverage
     * @param vendor
     * @param systemCpuLoad
     * @param physicalProcessorCount
     * @param logicalProcessorCount
     * @param systemCpuLoadBetweenTicks
     * @param systemSerialNumber
     * @param cpu64bit
     * @param processorCpuLoadBetweenTicks
     * @param family
     * @param name
     * @param systemCpuLoadTicks
     * @param systemUptime
     * @param identifier
     */
    public CentralProcessor(int logicalProcessorCount, int physicalProcessorCount, long systemUptime, long[][] processorCpuLoadTicks, String systemSerialNumber, long[] systemCpuLoadTicks, String name, double systemLoadAverage, double systemCpuLoad, String identifier, String family, String vendor, long vendorFreq, String model, String stepping, boolean cpu64bit, double[] processorCpuLoadBetweenTicks, double systemCpuLoadBetweenTicks) {
        super();
        this.logicalProcessorCount = logicalProcessorCount;
        this.physicalProcessorCount = physicalProcessorCount;
        this.systemUptime = systemUptime;
        this.processorCpuLoadTicks = processorCpuLoadTicks;
        this.systemSerialNumber = systemSerialNumber;
        this.systemCpuLoadTicks = systemCpuLoadTicks;
        this.name = name;
        this.systemLoadAverage = systemLoadAverage;
        this.systemCpuLoad = systemCpuLoad;
        this.identifier = identifier;
        this.family = family;
        this.vendor = vendor;
        this.vendorFreq = vendorFreq;
        this.model = model;
        this.stepping = stepping;
        this.cpu64bit = cpu64bit;
        this.processorCpuLoadBetweenTicks = processorCpuLoadBetweenTicks;
        this.systemCpuLoadBetweenTicks = systemCpuLoadBetweenTicks;
    }

    @JsonProperty("logicalProcessorCount")
    public Integer getLogicalProcessorCount() {
        return logicalProcessorCount;
    }

    @JsonProperty("logicalProcessorCount")
    public void setLogicalProcessorCount(int logicalProcessorCount) {
        this.logicalProcessorCount = logicalProcessorCount;
    }

    @JsonProperty("physicalProcessorCount")
    public Integer getPhysicalProcessorCount() {
        return physicalProcessorCount;
    }

    @JsonProperty("physicalProcessorCount")
    public void setPhysicalProcessorCount(int physicalProcessorCount) {
        this.physicalProcessorCount = physicalProcessorCount;
    }

    @JsonProperty("systemUptime")
    public long getSystemUptime() {
        return systemUptime;
    }

    @JsonProperty("systemUptime")
    public void setSystemUptime(long systemUptime) {
        this.systemUptime = systemUptime;
    }

    @JsonProperty("processorCpuLoadTicks")
    public long[][] getProcessorCpuLoadTicks() {
        return processorCpuLoadTicks;
    }

    @JsonProperty("processorCpuLoadTicks")
    public void setProcessorCpuLoadTicks(long[][] processorCpuLoadTicks) {
        this.processorCpuLoadTicks = processorCpuLoadTicks;
    }

    @JsonProperty("systemSerialNumber")
    public String getSystemSerialNumber() {
        return systemSerialNumber;
    }

    @JsonProperty("systemSerialNumber")
    public void setSystemSerialNumber(String systemSerialNumber) {
        this.systemSerialNumber = systemSerialNumber;
    }

    @JsonProperty("systemCpuLoadTicks")
    public long[] getSystemCpuLoadTicks() {
        return systemCpuLoadTicks;
    }

    @JsonProperty("systemCpuLoadTicks")
    public void setSystemCpuLoadTicks(long[] systemCpuLoadTicks) {
        this.systemCpuLoadTicks = systemCpuLoadTicks;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("systemLoadAverage")
    public double getSystemLoadAverage() {
        return systemLoadAverage;
    }

    @JsonProperty("systemLoadAverage")
    public void setSystemLoadAverage(double systemLoadAverage) {
        this.systemLoadAverage = systemLoadAverage;
    }

    @JsonProperty("systemCpuLoad")
    public Double getSystemCpuLoad() {
        return systemCpuLoad;
    }

    @JsonProperty("systemCpuLoad")
    public void setSystemCpuLoad(double systemCpuLoad) {
        this.systemCpuLoad = systemCpuLoad;
    }

    @JsonProperty("identifier")
    public String getIdentifier() {
        return identifier;
    }

    @JsonProperty("identifier")
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @JsonProperty("family")
    public String getFamily() {
        return family;
    }

    @JsonProperty("family")
    public void setFamily(String family) {
        this.family = family;
    }

    @JsonProperty("vendor")
    public String getVendor() {
        return vendor;
    }

    @JsonProperty("vendor")
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    @JsonProperty("vendorFreq")
    public long getVendorFreq() {
        return vendorFreq;
    }

    @JsonProperty("vendorFreq")
    public void setVendorFreq(long vendorFreq) {
        this.vendorFreq = vendorFreq;
    }

    @JsonProperty("model")
    public String getModel() {
        return model;
    }

    @JsonProperty("model")
    public void setModel(String model) {
        this.model = model;
    }

    @JsonProperty("stepping")
    public String getStepping() {
        return stepping;
    }

    @JsonProperty("stepping")
    public void setStepping(String stepping) {
        this.stepping = stepping;
    }

    @JsonProperty("cpu64bit")
    public boolean getCpu64bit() {
        return cpu64bit;
    }

    @JsonProperty("cpu64bit")
    public void setCpu64bit(boolean cpu64bit) {
        this.cpu64bit = cpu64bit;
    }

    @JsonProperty("processorCpuLoadBetweenTicks")
    public double[] getProcessorCpuLoadBetweenTicks() {
        return processorCpuLoadBetweenTicks;
    }

    @JsonProperty("processorCpuLoadBetweenTicks")
    public void setProcessorCpuLoadBetweenTicks(double[] processorCpuLoadBetweenTicks) {
        this.processorCpuLoadBetweenTicks = processorCpuLoadBetweenTicks;
    }

    @JsonProperty("systemCpuLoadBetweenTicks")
    public Double getSystemCpuLoadBetweenTicks() {
        return systemCpuLoadBetweenTicks;
    }

    @JsonProperty("systemCpuLoadBetweenTicks")
    public void setSystemCpuLoadBetweenTicks(double systemCpuLoadBetweenTicks) {
        this.systemCpuLoadBetweenTicks = systemCpuLoadBetweenTicks;
    }

}
