package com.krillsson.sysapi.dto.memory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "swapTotal",
        "swapUsed",
        "total",
        "available"
})
public class GlobalMemory {

    @JsonProperty("swapTotal")
    private long swapTotal;
    @JsonProperty("swapUsed")
    private long swapUsed;
    @JsonProperty("total")
    private long total;
    @JsonProperty("available")
    private long available;

    /**
     * No args constructor for use in serialization
     */
    public GlobalMemory() {
    }

    /**
     * @param total
     * @param swapUsed
     * @param swapTotal
     * @param available
     */
    public GlobalMemory(long swapTotal, long swapUsed, long total, long available) {
        super();
        this.swapTotal = swapTotal;
        this.swapUsed = swapUsed;
        this.total = total;
        this.available = available;
    }

    @JsonProperty("swapTotal")
    public long getSwapTotal() {
        return swapTotal;
    }

    @JsonProperty("swapTotal")
    public void setSwapTotal(long swapTotal) {
        this.swapTotal = swapTotal;
    }

    @JsonProperty("swapUsed")
    public long getSwapUsed() {
        return swapUsed;
    }

    @JsonProperty("swapUsed")
    public void setSwapUsed(long swapUsed) {
        this.swapUsed = swapUsed;
    }

    @JsonProperty("total")
    public long getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(long total) {
        this.total = total;
    }

    @JsonProperty("available")
    public long getAvailable() {
        return available;
    }

    @JsonProperty("available")
    public void setAvailable(long available) {
        this.available = available;
    }

}
