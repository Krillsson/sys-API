package com.krillsson.sysapi.dto.processes;

public class Process {


    private String name;

    private String path;

    private String commandLine;

    private String user;

    private String userID;

    private String group;

    private String groupID;

    private String state;

    private int processID;

    private int parentProcessID;

    private int threadCount;

    private int priority;

    private long virtualSize;

    private long residentSetSize;

    private double memoryPercent;

    private long kernelTime;

    private long userTime;

    private long upTime;

    private double cpuPercent;

    private long startTime;

    private long bytesRead;

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


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getPath() {
        return path;
    }


    public void setPath(String path) {
        this.path = path;
    }


    public String getCommandLine() {
        return commandLine;
    }


    public void setCommandLine(String commandLine) {
        this.commandLine = commandLine;
    }


    public String getUser() {
        return user;
    }


    public void setUser(String user) {
        this.user = user;
    }


    public String getUserID() {
        return userID;
    }


    public void setUserID(String userID) {
        this.userID = userID;
    }


    public String getGroup() {
        return group;
    }


    public void setGroup(String group) {
        this.group = group;
    }


    public String getGroupID() {
        return groupID;
    }


    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }


    public String getState() {
        return state;
    }


    public void setState(String state) {
        this.state = state;
    }


    public long getProcessID() {
        return processID;
    }


    public void setProcessID(int processID) {
        this.processID = processID;
    }


    public long getParentProcessID() {
        return parentProcessID;
    }


    public void setParentProcessID(int parentProcessID) {
        this.parentProcessID = parentProcessID;
    }


    public long getThreadCount() {
        return threadCount;
    }


    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }


    public long getPriority() {
        return priority;
    }


    public void setPriority(int priority) {
        this.priority = priority;
    }


    public long getVirtualSize() {
        return virtualSize;
    }


    public void setVirtualSize(long virtualSize) {
        this.virtualSize = virtualSize;
    }


    public long getResidentSetSize() {
        return residentSetSize;
    }


    public void setResidentSetSize(long residentSetSize) {
        this.residentSetSize = residentSetSize;
    }


    public double getMemoryPercent() {
        return memoryPercent;
    }


    public void setMemoryPercent(double memoryPercent) {
        this.memoryPercent = memoryPercent;
    }


    public long getKernelTime() {
        return kernelTime;
    }


    public void setKernelTime(long kernelTime) {
        this.kernelTime = kernelTime;
    }


    public long getUserTime() {
        return userTime;
    }


    public void setUserTime(long userTime) {
        this.userTime = userTime;
    }


    public long getUpTime() {
        return upTime;
    }


    public void setUpTime(long upTime) {
        this.upTime = upTime;
    }


    public double getCpuPercent() {
        return cpuPercent;
    }


    public void setCpuPercent(double cpuPercent) {
        this.cpuPercent = cpuPercent;
    }


    public long getStartTime() {
        return startTime;
    }


    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }


    public long getBytesRead() {
        return bytesRead;
    }


    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }


    public long getBytesWritten() {
        return bytesWritten;
    }


    public void setBytesWritten(long bytesWritten) {
        this.bytesWritten = bytesWritten;
    }

}
