package com.krillsson.sysapi.dto.power;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "remainingCapacity",
        "timeRemaining"
})
public class PowerSource {

    @JsonProperty("name")
    private String name;
    @JsonProperty("remainingCapacity")
    private double remainingCapacity;
    @JsonProperty("timeRemaining")
    private double timeRemaining;

    /**
     * No args constructor for use in serialization
     */
    public PowerSource() {
    }

    /**
     * @param remainingCapacity
     * @param name
     * @param timeRemaining
     */
    public PowerSource(String name, double remainingCapacity, double timeRemaining) {
        super();
        this.name = name;
        this.remainingCapacity = remainingCapacity;
        this.timeRemaining = timeRemaining;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("remainingCapacity")
    public double getRemainingCapacity() {
        return remainingCapacity;
    }

    @JsonProperty("remainingCapacity")
    public void setRemainingCapacity(double remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }

    @JsonProperty("timeRemaining")
    public double getTimeRemaining() {
        return timeRemaining;
    }

    @JsonProperty("timeRemaining")
    public void setTimeRemaining(double timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

}
