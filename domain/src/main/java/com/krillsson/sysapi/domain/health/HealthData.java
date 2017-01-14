package com.krillsson.sysapi.domain.health;

public class HealthData {
    private final String description;
    private final double data;
    private final DataType dataType;

    public HealthData(String description, double data, DataType dataType) {
        this.description = description;
        this.data = data;
        this.dataType = dataType;
    }

    public String getDescription() {
        return description;
    }

    public double getData() {
        return data;
    }

    public DataType getDataType() {
        return dataType;
    }
}
