package com.krillsson.sysapi.periodictasks

import com.krillsson.sysapi.util.logger
import io.dropwizard.jobs.Job
import org.quartz.JobExecutionContext
import java.time.Duration
import java.time.Instant
import java.util.*

abstract class TaskExecutorJob : Job() {

    abstract val retryIntervalSeconds: Long

    sealed interface TaskContainer {
        val task: Task
        val id: UUID

        data class Active(
            override val task: Task,
            override val id: UUID
        ) : TaskContainer

        data class Evicted(
            override val task: Task,
            override val id: UUID,
            val evictedAt: Instant,
            val error: Throwable
        ) : TaskContainer
    }

    private val logger by logger()
    private val activeContainers: MutableList<TaskContainer.Active> = mutableListOf()
    private val evictedContainers: MutableList<TaskContainer.Evicted> = mutableListOf()


    fun register(listener: Task) {
        logger.debug("Register ${listener::class.simpleName} to ${this::class.simpleName} job")
        activeContainers.add(TaskContainer.Active(listener, UUID.randomUUID()))
    }

    fun unregister(task: Task) {
        activeContainers.removeIf { it.task == task }
    }

    override fun doJob(context: JobExecutionContext?) {
        logger.trace("Execute ${this::class.simpleName} tasks")
        retryEvicted()
        val evictedContainers = mutableListOf<TaskContainer.Evicted>()
        for (container in activeContainers) {
            try {
                logger.trace("Perform ${container.task.key.name}")
                container.task.run()
            } catch (e: Throwable) {
                logger.error(
                    "Error while executing task ${container.task::class.java.simpleName}/${container.id} Evicting...",
                    e
                )
                val evicted = TaskContainer.Evicted(container.task, container.id, Instant.now(), e)
                evictedContainers += evicted
            }
        }
        evict(evictedContainers)
    }

    private fun retryEvicted() {
        val evictedToConsider = evictedContainers.toList()
        val now = Instant.now()
        evictedToConsider.forEach { evicted ->
            if (Duration.between(evicted.evictedAt, now).seconds >= retryIntervalSeconds) {
                logger.info("Retrying task ${evicted.task::class.java.simpleName}/${evicted.id} after ${retryIntervalSeconds}s")
                evictedContainers.removeIf { it.id == evicted.id }
                activeContainers.add(
                    TaskContainer.Active(evicted.task, evicted.id)
                )
            }
        }
    }

    private fun evict(containersToEvict: List<TaskContainer.Evicted>) {
        containersToEvict.forEach { evicted ->
            activeContainers.removeIf { it.id == evicted.id }
            evictedContainers.add(evicted)
        }
    }
}