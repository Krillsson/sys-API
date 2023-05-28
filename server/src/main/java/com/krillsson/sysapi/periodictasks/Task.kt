package com.krillsson.sysapi.periodictasks

interface Task {
    enum class Key {
        RecordCpuLoad,
        UpdateProcessesList,
        CheckConnectivity,
        RecordDiskReadWriteRate,
        RecordNetworkUploadDownloadRate,
        StoreMetricHistoryEntry,
        CheckMonitors
    }
    
    val key: Key
    
    val defaultInterval: TaskInterval
    fun run()
}