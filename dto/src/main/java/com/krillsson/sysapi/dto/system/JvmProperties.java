package com.krillsson.sysapi.dto.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "properties"
})
public class JvmProperties {

    @JsonProperty("properties")
    private Map<String, String> properties;

    /**
     * No args constructor for use in serialization
     */
    public JvmProperties() {
    }

    /**
     * @param properties
     */
    public JvmProperties(Map<String, String> properties) {
        super();
        this.properties = properties;
    }

    @JsonProperty("properties")
    public Map<String, String> getProperties() {
        return properties;
    }

    @JsonProperty("properties")
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

}
