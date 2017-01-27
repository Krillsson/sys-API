/*
 * Sys-Api (https://github.com/Krillsson/sys-api)
 *
 * Copyright 2017 Christian Jensen / Krillsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Maintainers:
 * contact[at]christian-jensen[dot]se
 */

package com.krillsson.sysapi.dto.sensors;

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
    private double data;
    @JsonProperty("dataType")
    private DataType dataType;

    /**
     * No args constructor for use in serialization
     */
    public HealthData() {
    }

    public HealthData(String description, double data, DataType dataType) {
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
    public double getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(double data) {
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
