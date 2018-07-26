package com.krillsson.sysapi.dto.metadata;

public class Meta {


    private String[] endpoints;

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


    public String[] getEndpoints() {
        return endpoints;
    }


    public void setEndpoints(String[] endpoints) {
        this.endpoints = endpoints;
    }


    public String getVersion() {
        return version;
    }


    public void setVersion(String version) {
        this.version = version;
    }
}
