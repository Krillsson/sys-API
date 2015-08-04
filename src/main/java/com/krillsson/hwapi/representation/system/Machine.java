package com.krillsson.hwapi.representation.system;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Machine {

    private String hostname;
    private double uptime;
    private OperatingSystem operatingSystem;
    private List<UserInfo> users;
    private JvmProperties jvmProperties;

    public Machine(String hostname, List<UserInfo> userInfo, double uptime, OperatingSystem operatingSystem, JvmProperties jvmProperties) {
        this.hostname = hostname;
        this.users = userInfo;
        this.operatingSystem = operatingSystem;
        this.uptime = uptime;
        this.jvmProperties = jvmProperties;
    }

    @JsonProperty
    public String getHostname() {
        return hostname;
    }

    @JsonProperty
    public List<UserInfo> getUsers() {
        return users;
    }

    @JsonProperty
    public double getUptime() {
        return uptime;
    }

    @JsonProperty
    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    @JsonProperty
    public JvmProperties getJvmProperties() {
        return jvmProperties;
    }
}
