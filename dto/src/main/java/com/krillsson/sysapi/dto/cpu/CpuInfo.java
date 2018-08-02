package com.krillsson.sysapi.dto.cpu;


public class CpuInfo {


    private CentralProcessor centralProcessor;

    /**
     * No args constructor for use in serialization
     */
    public CpuInfo() {
    }

    /**
     * @param centralProcessor
     */
    public CpuInfo(CentralProcessor centralProcessor) {
        super();
        this.centralProcessor = centralProcessor;
    }


    public CentralProcessor getCentralProcessor() {
        return centralProcessor;
    }


    public void setCentralProcessor(CentralProcessor centralProcessor) {
        this.centralProcessor = centralProcessor;
    }

}
