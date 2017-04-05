package com.krillsson.sysapi.dto.processes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "path",
        "commandLine",
        "user",
        "userID",
        "group",
        "groupID",
        "state",
        "processID",
        "parentProcessID",
        "threadCount",
        "priority",
        "virtualSize",
        "residentSetSize",
        "memoryPercent",
        "kernelTime",
        "userTime",
        "upTime",
        "cpuPercent",
        "startTime",
        "bytesRead",
        "bytesWritten"
})
public class Process {

    @JsonProperty("name")
    private String name;
    @JsonProperty("path")
    private String path;
    @JsonProperty("commandLine")
    private String commandLine;
    @JsonProperty("user")
    private String user;
    @JsonProperty("userID")
    private String userID;
    @JsonProperty("group")
    private String group;
    @JsonProperty("groupID")
    private String groupID;
    @JsonProperty("state")
    private String state;
    @JsonProperty("processID")
    private int processID;
    @JsonProperty("parentProcessID")
    private int parentProcessID;
    @JsonProperty("threadCount")
    private int threadCount;
    @JsonProperty("priority")
    private int priority;
    @JsonProperty("virtualSize")
    private long virtualSize;
    @JsonProperty("residentSetSize")
    private long residentSetSize;
    @JsonProperty("memoryPercent")
    private double memoryPercent;
    @JsonProperty("kernelTime")
    private long kernelTime;
    @JsonProperty("userTime")
    private long userTime;
    @JsonProperty("upTime")
    private long upTime;
    @JsonProperty("cpuPercent")
    private double cpuPercent;
    @JsonProperty("startTime")
    private long startTime;
    @JsonProperty("bytesRead")
    private long bytesRead;
    @JsonProperty("bytesWritten")
    private long bytesWritten;

    /**
     * No args constructor for use in serialization
     */
    public Process() {
    }

    /**
     * @param name
     * @param path
     * @param state
     * @param commandLine
     * @param user
     * @param userID
     * @param group
     * @param groupID
     * @param processID
     * @param parentProcessID
     * @param threadCount
     * @param priority
     * @param virtualSize
     * @param residentSetSize
     * @param memoryPercent
     * @param kernelTime
     * @param userTime
     * @param upTime
     * @param cpuPercent
     * @param startTime
     * @param bytesRead
     * @param bytesWritten
     */
    public Process(String name, String path, String commandLine, String user, String userID, String group, String groupID, String state, int processID, int parentProcessID, int threadCount, int priority, long virtualSize, long residentSetSize, double memoryPercent, long kernelTime, long userTime, long upTime, double cpuPercent, long startTime, long bytesRead, long bytesWritten) {
        super();
        this.name = name;
        this.path = path;
        this.commandLine = commandLine;
        this.user = user;
        this.userID = userID;
        this.group = group;
        this.groupID = groupID;
        this.state = state;
        this.processID = processID;
        this.parentProcessID = parentProcessID;
        this.threadCount = threadCount;
        this.priority = priority;
        this.virtualSize = virtualSize;
        this.residentSetSize = residentSetSize;
        this.memoryPercent = memoryPercent;
        this.kernelTime = kernelTime;
        this.userTime = userTime;
        this.upTime = upTime;
        this.cpuPercent = cpuPercent;
        this.startTime = startTime;
        this.bytesRead = bytesRead;
        this.bytesWritten = bytesWritten;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    @JsonProperty("commandLine")
    public String getCommandLine() {
        return commandLine;
    }

    @JsonProperty("commandLine")
    public void setCommandLine(String commandLine) {
        this.commandLine = commandLine;
    }

    @JsonProperty("user")
    public String getUser() {
        return user;
    }

    @JsonProperty("user")
    public void setUser(String user) {
        this.user = user;
    }

    @JsonProperty("userID")
    public String getUserID() {
        return userID;
    }

    @JsonProperty("userID")
    public void setUserID(String userID) {
        this.userID = userID;
    }

    @JsonProperty("group")
    public String getGroup() {
        return group;
    }

    @JsonProperty("group")
    public void setGroup(String group) {
        this.group = group;
    }

    @JsonProperty("groupID")
    public String getGroupID() {
        return groupID;
    }

    @JsonProperty("groupID")
    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("processID")
    public long getProcessID() {
        return processID;
    }

    @JsonProperty("processID")
    public void setProcessID(int processID) {
        this.processID = processID;
    }

    @JsonProperty("parentProcessID")
    public long getParentProcessID() {
        return parentProcessID;
    }

    @JsonProperty("parentProcessID")
    public void setParentProcessID(int parentProcessID) {
        this.parentProcessID = parentProcessID;
    }

    @JsonProperty("threadCount")
    public long getThreadCount() {
        return threadCount;
    }

    @JsonProperty("threadCount")
    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    @JsonProperty("priority")
    public long getPriority() {
        return priority;
    }

    @JsonProperty("priority")
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @JsonProperty("virtualSize")
    public long getVirtualSize() {
        return virtualSize;
    }

    @JsonProperty("virtualSize")
    public void setVirtualSize(long virtualSize) {
        this.virtualSize = virtualSize;
    }

    @JsonProperty("residentSetSize")
    public long getResidentSetSize() {
        return residentSetSize;
    }

    @JsonProperty("residentSetSize")
    public void setResidentSetSize(long residentSetSize) {
        this.residentSetSize = residentSetSize;
    }

    @JsonProperty("memoryPercent")
    public double getMemoryPercent() {
        return memoryPercent;
    }

    @JsonProperty("memoryPercent")
    public void setMemoryPercent(double memoryPercent) {
        this.memoryPercent = memoryPercent;
    }

    @JsonProperty("kernelTime")
    public long getKernelTime() {
        return kernelTime;
    }

    @JsonProperty("kernelTime")
    public void setKernelTime(long kernelTime) {
        this.kernelTime = kernelTime;
    }

    @JsonProperty("userTime")
    public long getUserTime() {
        return userTime;
    }

    @JsonProperty("userTime")
    public void setUserTime(long userTime) {
        this.userTime = userTime;
    }

    @JsonProperty("upTime")
    public long getUpTime() {
        return upTime;
    }

    @JsonProperty("upTime")
    public void setUpTime(long upTime) {
        this.upTime = upTime;
    }

    @JsonProperty("cpuPercent")
    public double getCpuPercent() {
        return cpuPercent;
    }

    @JsonProperty("cpuPercent")
    public void setCpuPercent(double cpuPercent) {
        this.cpuPercent = cpuPercent;
    }

    @JsonProperty("startTime")
    public long getStartTime() {
        return startTime;
    }

    @JsonProperty("startTime")
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @JsonProperty("bytesRead")
    public long getBytesRead() {
        return bytesRead;
    }

    @JsonProperty("bytesRead")
    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    @JsonProperty("bytesWritten")
    public long getBytesWritten() {
        return bytesWritten;
    }

    @JsonProperty("bytesWritten")
    public void setBytesWritten(long bytesWritten) {
        this.bytesWritten = bytesWritten;
    }

}
