package com.krillsson.sysapi.core.metrics.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.krillsson.sysapi.config.CacheConfiguration;
import com.krillsson.sysapi.core.domain.processes.Process;
import com.krillsson.sysapi.core.domain.processes.ProcessesInfo;
import com.krillsson.sysapi.core.metrics.ProcessesMetrics;
import oshi.software.os.OperatingSystem;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Using the process sort and limit arguments as the key for the ProcessInfo cache will cause
 * a real call for when one tiny thing in arguments changes. To prevent a huge cache we set max size of this cache to 1.
 * It might turn out that we don't want to cache processes at all because of the size in RAM.
 */
public class CachingProcessesMetrics implements ProcessesMetrics {

    private final LoadingCache<Integer, Optional<Process>> processQueryCache;
    private final Cache<String, ProcessesInfo> processesInfoCache;
    private final ProcessesMetrics processesMetrics;


    public CachingProcessesMetrics(ProcessesMetrics processesMetrics, CacheConfiguration cacheConfiguration) {
        this.processesMetrics = processesMetrics;
        this.processesInfoCache = CacheBuilder.newBuilder()
                .maximumSize(1)
                .expireAfterWrite(cacheConfiguration.getDuration(), cacheConfiguration.getUnit())
                .build();
        this.processQueryCache = CacheBuilder.newBuilder()
                .expireAfterWrite(cacheConfiguration.getDuration(), cacheConfiguration.getUnit())
                .build(new CacheLoader<Integer, Optional<Process>>() {
                    @Override
                    public Optional<Process> load(Integer s) throws Exception {
                        return processesMetrics.getProcessByPid(s);
                    }
                });
    }

    @Override
    public ProcessesInfo processesInfo(OperatingSystem.ProcessSort sortBy, int limit) {
        try {
            return processesInfoCache.get(
                    sortBy.name() + ":" + String.valueOf(limit),
                    () -> processesMetrics.processesInfo(sortBy, limit)
            );
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<Process> getProcessByPid(int pid) {
        return processQueryCache.getUnchecked(pid);
    }


}
