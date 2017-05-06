package com.krillsson.sysapi.dto.metadata;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "endpoints",
        "version"
})
public class Meta {

    @JsonProperty("endpoints")
    private String[] endpoints;
    @JsonProperty("version")
    private String version;

    /**
     * No args constructor for use in serialization
     */
    public Meta() {
    }

    /***
     * @param endpoints
     * @param version
     */
    public Meta(String[] endpoints, String version) {
        this.endpoints = endpoints;
        this.version = version;
    }

    @JsonProperty("endpoints")
    public String[] getEndpoints() {
        return endpoints;
    }

    @JsonProperty("endpoints")
    public void setEndpoints(String[] endpoints) {
        this.endpoints = endpoints;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }
}
