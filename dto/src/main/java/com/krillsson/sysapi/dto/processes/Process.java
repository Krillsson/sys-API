package com.krillsson.sysapi.dto.processes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "path",
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
    @JsonProperty("state")
    private String state;
    @JsonProperty("processID")
    private Integer processID;
    @JsonProperty("parentProcessID")
    private Integer parentProcessID;
    @JsonProperty("threadCount")
    private Integer threadCount;
    @JsonProperty("priority")
    private Integer priority;
    @JsonProperty("virtualSize")
    private Integer virtualSize;
    @JsonProperty("residentSetSize")
    private Integer residentSetSize;
    @JsonProperty("memoryPercent")
    private Double memoryPercent;
    @JsonProperty("kernelTime")
    private Integer kernelTime;
    @JsonProperty("userTime")
    private Integer userTime;
    @JsonProperty("upTime")
    private Integer upTime;
    @JsonProperty("cpuPercent")
    private Double cpuPercent;
    @JsonProperty("startTime")
    private Integer startTime;
    @JsonProperty("bytesRead")
    private Integer bytesRead;
    @JsonProperty("bytesWritten")
    private Integer bytesWritten;

    /**
     * No args constructor for use in serialization
     */
    public Process() {
    }

    /**
     * @param residentSetSize
     * @param bytesRead
     * @param processID
     * @param state
     * @param parentProcessID
     * @param userTime
     * @param memoryPercent
     * @param virtualSize
     * @param startTime
     * @param bytesWritten
     * @param upTime
     * @param kernelTime
     * @param priority
     * @param cpuPercent
     * @param name
     * @param threadCount
     * @param path
     */
    public Process(String name, String path, String state, Integer processID, Integer parentProcessID, Integer threadCount, Integer priority, Integer virtualSize, Integer residentSetSize, Double memoryPercent, Integer kernelTime, Integer userTime, Integer upTime, Double cpuPercent, Integer startTime, Integer bytesRead, Integer bytesWritten) {
        super();
        this.name = name;
        this.path = path;
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

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("processID")
    public Integer getProcessID() {
        return processID;
    }

    @JsonProperty("processID")
    public void setProcessID(Integer processID) {
        this.processID = processID;
    }

    @JsonProperty("parentProcessID")
    public Integer getParentProcessID() {
        return parentProcessID;
    }

    @JsonProperty("parentProcessID")
    public void setParentProcessID(Integer parentProcessID) {
        this.parentProcessID = parentProcessID;
    }

    @JsonProperty("threadCount")
    public Integer getThreadCount() {
        return threadCount;
    }

    @JsonProperty("threadCount")
    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }

    @JsonProperty("priority")
    public Integer getPriority() {
        return priority;
    }

    @JsonProperty("priority")
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @JsonProperty("virtualSize")
    public Integer getVirtualSize() {
        return virtualSize;
    }

    @JsonProperty("virtualSize")
    public void setVirtualSize(Integer virtualSize) {
        this.virtualSize = virtualSize;
    }

    @JsonProperty("residentSetSize")
    public Integer getResidentSetSize() {
        return residentSetSize;
    }

    @JsonProperty("residentSetSize")
    public void setResidentSetSize(Integer residentSetSize) {
        this.residentSetSize = residentSetSize;
    }

    @JsonProperty("memoryPercent")
    public Double getMemoryPercent() {
        return memoryPercent;
    }

    @JsonProperty("memoryPercent")
    public void setMemoryPercent(Double memoryPercent) {
        this.memoryPercent = memoryPercent;
    }

    @JsonProperty("kernelTime")
    public Integer getKernelTime() {
        return kernelTime;
    }

    @JsonProperty("kernelTime")
    public void setKernelTime(Integer kernelTime) {
        this.kernelTime = kernelTime;
    }

    @JsonProperty("userTime")
    public Integer getUserTime() {
        return userTime;
    }

    @JsonProperty("userTime")
    public void setUserTime(Integer userTime) {
        this.userTime = userTime;
    }

    @JsonProperty("upTime")
    public Integer getUpTime() {
        return upTime;
    }

    @JsonProperty("upTime")
    public void setUpTime(Integer upTime) {
        this.upTime = upTime;
    }

    @JsonProperty("cpuPercent")
    public Double getCpuPercent() {
        return cpuPercent;
    }

    @JsonProperty("cpuPercent")
    public void setCpuPercent(Double cpuPercent) {
        this.cpuPercent = cpuPercent;
    }

    @JsonProperty("startTime")
    public Integer getStartTime() {
        return startTime;
    }

    @JsonProperty("startTime")
    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    @JsonProperty("bytesRead")
    public Integer getBytesRead() {
        return bytesRead;
    }

    @JsonProperty("bytesRead")
    public void setBytesRead(Integer bytesRead) {
        this.bytesRead = bytesRead;
    }

    @JsonProperty("bytesWritten")
    public Integer getBytesWritten() {
        return bytesWritten;
    }

    @JsonProperty("bytesWritten")
    public void setBytesWritten(Integer bytesWritten) {
        this.bytesWritten = bytesWritten;
    }

}
