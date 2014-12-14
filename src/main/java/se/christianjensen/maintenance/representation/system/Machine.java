package se.christianjensen.maintenance.representation.system;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Machine {

    private String hostname;
    private double uptime;
    private OperatingSystem operatingSystem;
    private List<UserInfo> users;

    public Machine(String hostname, List<UserInfo> userInfo, double uptime, OperatingSystem operatingSystem) {
        this.hostname = hostname;
        this.users = userInfo;
        this.operatingSystem = operatingSystem;
        this.uptime = uptime;
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


}
