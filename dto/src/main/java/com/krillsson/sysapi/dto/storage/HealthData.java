package com.krillsson.sysapi.dto.storage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "description",
        "data",
        "dataType"
})
public class HealthData {

    @JsonProperty("description")
    private String description;
    @JsonProperty("data")
    private Integer data;
    @JsonProperty("dataType")
    private DataType dataType;

    /**
     * No args constructor for use in serialization
     */
    public HealthData() {
    }

    /**
     * @param dataType
     * @param description
     * @param data
     */
    public HealthData(String description, Integer data, DataType dataType) {
        super();
        this.description = description;
        this.data = data;
        this.dataType = dataType;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("data")
    public Integer getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Integer data) {
        this.data = data;
    }

    @JsonProperty("dataType")
    public DataType getDataType() {
        return dataType;
    }

    @JsonProperty("dataType")
    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

}
