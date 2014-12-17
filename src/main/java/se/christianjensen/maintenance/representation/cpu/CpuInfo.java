package se.christianjensen.maintenance.representation.cpu;


import com.fasterxml.jackson.annotation.JsonProperty;

public class CpuInfo {
    private final String vendor;
    private final String model;
    private final int mhz;
    private final long cacheSize;
    private final int totalCores;
    private final int totalSockets;
    private final int coresPerSocket;

    public CpuInfo(String vendor, String model, int mhz, long cacheSize, int totalCores, int totalSockets, int coresPerSocket) {
        this.vendor = vendor;
        this.model = model;
        this.mhz = mhz;
        this.cacheSize = cacheSize;
        this.totalCores = totalCores;
        this.totalSockets = totalSockets;
        this.coresPerSocket = coresPerSocket;
    }

    public static CpuInfo fromSigarBean(org.hyperic.sigar.CpuInfo sigarCpuInfo) {
        return new CpuInfo(sigarCpuInfo.getVendor()
                , sigarCpuInfo.getModel().trim()
                , sigarCpuInfo.getMhz()
                , sigarCpuInfo.getCacheSize()
                , sigarCpuInfo.getTotalCores()
                , sigarCpuInfo.getTotalSockets()
                , sigarCpuInfo.getCoresPerSocket());
    }

    @JsonProperty
    public String getVendor() {
        return vendor;
    }

    @JsonProperty
    public String getModel() {
        return model;
    }

    @JsonProperty
    public int getMhz() {
        return mhz;
    }

    @JsonProperty
    public long getCacheSize() {
        return cacheSize;
    }

    @JsonProperty
    public int getTotalCores() {
        return totalCores;
    }

    @JsonProperty
    public int getTotalSockets() {
        return totalSockets;
    }

    @JsonProperty
    public int getCoresPerSocket() {
        return coresPerSocket;
    }
}
