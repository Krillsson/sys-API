package com.krillsson.sysapi.domain.drive;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LifecycleData {
    private final String description;
    private final double data;

    public LifecycleData(String description, double data) {
        this.description = description;
        this.data = data;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }

    @JsonProperty
    public double getData() {
        return data;
    }
}
