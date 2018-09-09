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
package com.krillsson.sysapi.core.domain.drives;

import java.util.List;

public class Drive {
    private final String model;
    private final String name;
    private final String serial;
    private final long sizeBytes;
    private final List<Partition> partitions;
    private final OsPartition diskOsPartition;

    public Drive(String model, String name, String serial, long sizeBytes, OsPartition diskOsPartition, List<Partition> partitions) {
        this.model = model;
        this.name = name;
        this.serial = serial;
        this.sizeBytes = sizeBytes;
        this.diskOsPartition = diskOsPartition;
        this.partitions = partitions;
    }

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public String getSerial() {
        return serial;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public List<Partition> getPartitions() {
        return partitions;
    }

    public OsPartition getDiskOsPartition() {
        return diskOsPartition;
    }
}
