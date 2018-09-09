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
package com.krillsson.sysapi.dto.drives;

import java.util.List;

public class Drive {
    private String model;
    private String name;
    private String serial;
    private long sizeBytes;
    private List<Partition> partitions;
    private OsPartition diskOsPartition;

    public Drive(String model, String name, String serial, long sizeBytes, OsPartition diskOsPartition, List<Partition> partitions) {
        this.model = model;
        this.name = name;
        this.serial = serial;
        this.sizeBytes = sizeBytes;
        this.diskOsPartition = diskOsPartition;
        this.partitions = partitions;
    }

    public Drive() {
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public List<Partition> getPartitions() {
        return partitions;
    }

    public void setPartitions(List<Partition> partitions) {
        this.partitions = partitions;
    }

    public OsPartition getDiskOsPartition() {
        return diskOsPartition;
    }

    public void setDiskOsPartition(OsPartition diskOsPartition) {
        this.diskOsPartition = diskOsPartition;
    }
}
