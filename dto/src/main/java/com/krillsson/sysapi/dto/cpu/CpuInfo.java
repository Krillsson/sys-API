package com.krillsson.sysapi.dto.cpu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "centralProcessor",
})
public class CpuInfo {

    @JsonProperty("centralProcessor")
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

    @JsonProperty("centralProcessor")
    public CentralProcessor getCentralProcessor() {
        return centralProcessor;
    }

    @JsonProperty("centralProcessor")
    public void setCentralProcessor(CentralProcessor centralProcessor) {
        this.centralProcessor = centralProcessor;
    }

}
