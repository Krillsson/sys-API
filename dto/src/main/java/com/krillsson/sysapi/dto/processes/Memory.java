package com.krillsson.sysapi.dto.processes;

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
public class Memory {

    @JsonProperty("swapTotal")
    private Integer swapTotal;
    @JsonProperty("swapUsed")
    private Integer swapUsed;
    @JsonProperty("total")
    private Integer total;
    @JsonProperty("available")
    private Integer available;

    /**
     * No args constructor for use in serialization
     */
    public Memory() {
    }

    /**
     * @param total
     * @param swapUsed
     * @param swapTotal
     * @param available
     */
    public Memory(Integer swapTotal, Integer swapUsed, Integer total, Integer available) {
        super();
        this.swapTotal = swapTotal;
        this.swapUsed = swapUsed;
        this.total = total;
        this.available = available;
    }

    @JsonProperty("swapTotal")
    public Integer getSwapTotal() {
        return swapTotal;
    }

    @JsonProperty("swapTotal")
    public void setSwapTotal(Integer swapTotal) {
        this.swapTotal = swapTotal;
    }

    @JsonProperty("swapUsed")
    public Integer getSwapUsed() {
        return swapUsed;
    }

    @JsonProperty("swapUsed")
    public void setSwapUsed(Integer swapUsed) {
        this.swapUsed = swapUsed;
    }

    @JsonProperty("total")
    public Integer getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(Integer total) {
        this.total = total;
    }

    @JsonProperty("available")
    public Integer getAvailable() {
        return available;
    }

    @JsonProperty("available")
    public void setAvailable(Integer available) {
        this.available = available;
    }

}
