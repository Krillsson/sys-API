package com.krillsson.sysapi.core.metrics;

import com.krillsson.sysapi.core.domain.memory.MemoryLoad;

public interface MemoryMetrics {
    MemoryLoad memoryLoad();
}
