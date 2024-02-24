package com.krillsson.sysapi.config

import com.krillsson.sysapi.periodictasks.Task
import com.krillsson.sysapi.periodictasks.TaskInterval

data class TasksConfiguration(
    val interval: String,
    val perform: List<Task.Key>,
)

val defaultTasksValue = mapOf(
    TaskInterval.Often to TasksConfiguration(
        "5s",
        listOf(Task.Key.RecordCpuLoad, Task.Key.RecordNetworkUploadDownloadRate)
    ),
    TaskInterval.LessOften to TasksConfiguration(
        "15s",
        listOf(Task.Key.UpdateProcessesList, Task.Key.CheckMonitors, Task.Key.RecordDiskReadWriteRate)
    ),
    TaskInterval.Seldom to TasksConfiguration(
        "5min",
        listOf(Task.Key.CheckConnectivity)
    ),
    TaskInterval.VerySeldom to TasksConfiguration(
        "30min",
        listOf(Task.Key.StoreMetricHistoryEntry, Task.Key.CheckUpdate)
    )
)

