package se.christianjensen.maintenance.representation.system;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
* Created by christian on 2014-11-30.
*/
public class Machine {
    private String hostname;
    private double uptime;
    private OperatingSystem operatingSystem;

    public Machine(String hostname, double uptime, OperatingSystem operatingSystem) {
        this.hostname = hostname;
        this.operatingSystem = operatingSystem;
        this.uptime = uptime;
    }

    @JsonProperty
    public String getHostname() {
        return hostname;
    }

    @JsonProperty
    public double getUptime() {
        return uptime;
    }

    @JsonProperty
    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }


}
