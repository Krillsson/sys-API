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
package com.krillsson.sysapi.core.domain.storage;

import java.util.List;
import java.util.Optional;

public class DiskInfo {
    private final String model;
    private final String name;
    private final String serial;
    private final List<DiskPartition> diskPartitions;
    private final DiskOsPartition diskOsPartition;

    public DiskInfo(String model, String name, String serial, DiskOsPartition diskOsPartition, List<DiskPartition> diskPartitions) {
        this.model = model;
        this.name = name;
        this.serial = serial;
        this.diskOsPartition = diskOsPartition;
        this.diskPartitions = diskPartitions;
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

    public List<DiskPartition> getDiskPartitions() {
        return diskPartitions;
    }

    public DiskOsPartition getDiskOsPartition() {
        return diskOsPartition;
    }
}
