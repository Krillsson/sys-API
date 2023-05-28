package com.krillsson.sysapi.periodictasks

import io.dropwizard.jobs.annotations.DelayStart
import io.dropwizard.jobs.annotations.Every
import java.util.concurrent.TimeUnit

enum class TaskInterval {
    Often,
    LessOften,
    Seldom,
    VerySeldom
}

@DelayStart("5s")
@Every("\${VerySeldom}", jobName = "Very seldom job")
class VerySeldomTasksJob() : TaskExecutorJob() {
    override val retryIntervalSeconds: Long
        get() = TimeUnit.HOURS.toSeconds(1)
}

@DelayStart("15s")
@Every("\${Seldom}", jobName = "Seldom job")
class SeldomTasksJob : TaskExecutorJob() {
    override val retryIntervalSeconds: Long
        get() = TimeUnit.MINUTES.toSeconds(10)
}

@DelayStart("15s")
@Every("\${LessOften}", jobName = "Less often job")
class LessOftenTasksJob : TaskExecutorJob() {
    override val retryIntervalSeconds: Long
        get() = TimeUnit.MINUTES.toSeconds(1)
}

@DelayStart("15s")
@Every("\${Often}", jobName = "Often job")
class OftenTasksJob : TaskExecutorJob() {
    override val retryIntervalSeconds: Long
        get() = TimeUnit.SECONDS.toSeconds(30)
}

