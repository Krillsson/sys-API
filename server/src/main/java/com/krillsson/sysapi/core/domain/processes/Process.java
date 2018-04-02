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

package com.krillsson.sysapi.core.domain.processes;

import oshi.hardware.GlobalMemory;
import oshi.software.os.OSProcess;

public class Process {
    private final String name;
    private final String path;
    private final String commandLine;
    private final String user;
    private final String userID;
    private final String group;
    private final String groupID;
    private final OSProcess.State state;
    private final int processID;
    private final int parentProcessID;
    private final int threadCount;
    private final int priority;
    private final long virtualSize;
    private final long residentSetSize;
    private final double memoryPercent;
    private final long kernelTime;
    private final long userTime;
    private final long upTime;
    private final double cpuPercent;
    private final long startTime;
    private final long bytesRead;
    private final long bytesWritten;

    public Process(String name,
                   String path,
                   String commandLine,
                   String user,
                   String userID,
                   String group,
                   String groupID,
                   OSProcess.State state,
                   int processID,
                   int parentProcessID,
                   int threadCount,
                   int priority,
                   long virtualSize,
                   long residentSetSize,
                   double memoryPercent,
                   long kernelTime,
                   long userTime,
                   long upTime,
                   double cpuPercent,
                   long startTime,
                   long bytesRead,
                   long bytesWritten) {
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

    public static Process create(OSProcess process, GlobalMemory memory) {
        return new Process(process.getName(),
                           process.getPath(),
                           process.getCommandLine(),
                           process.getUser(),
                           process.getUserID(),
                           process.getGroup(),
                           process.getGroupID(),
                           process.getState(),
                           process.getProcessID(),
                           process.getParentProcessID(),
                           process.getThreadCount(),
                           process.getPriority(),
                           process.getVirtualSize(),
                           process.getResidentSetSize(),
                           100d * process.getResidentSetSize() / memory.getTotal(),
                           process.getKernelTime(), process.getUserTime(),
                           process.getUpTime(),
                           100d * (process.getKernelTime() + process.getUserTime()) / process.getUpTime(),
                           process.getStartTime(),
                           process.getBytesRead(),
                           process.getBytesWritten()
        );
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getCommandLine() {
        return commandLine;
    }

    public String getUser() {
        return user;
    }

    public String getUserID() {
        return userID;
    }

    public String getGroup() {
        return group;
    }

    public String getGroupID() {
        return groupID;
    }

    public OSProcess.State getState() {
        return state;
    }

    public int getProcessID() {
        return processID;
    }

    public int getParentProcessID() {
        return parentProcessID;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public int getPriority() {
        return priority;
    }

    public long getVirtualSize() {
        return virtualSize;
    }

    public long getResidentSetSize() {
        return residentSetSize;
    }

    public double getMemoryPercent() {
        return memoryPercent;
    }

    public long getKernelTime() {
        return kernelTime;
    }

    public long getUserTime() {
        return userTime;
    }

    public long getUpTime() {
        return upTime;
    }

    public double getCpuPercent() {
        return cpuPercent;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public long getBytesWritten() {
        return bytesWritten;
    }
}
