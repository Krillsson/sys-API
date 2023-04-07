package com.krillsson.sysapi.util

import io.dropwizard.lifecycle.Managed
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class PeriodicTaskManager(
    private val executorService: ScheduledExecutorService,
    private val measurementIntervalSeconds: Long,
    private val retryIntervalSeconds: Long
) : Managed {
    interface Task {
        fun run()
    }

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
        activeContainers.add(TaskContainer.Active(listener, UUID.randomUUID()))
    }

    fun unregister(task: Task) {
        activeContainers.removeIf { it.task == task }
    }

    @Throws(Exception::class)
    override fun start() {
        executorService.scheduleAtFixedRate({ execute() }, 1, measurementIntervalSeconds, TimeUnit.SECONDS)
    }

    private fun execute() {
        retryEvicted()
        val evictedContainers = mutableListOf<TaskContainer.Evicted>()
        for (container in activeContainers) {
            try {
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

    @Throws(Exception::class)
    override fun stop() {
        executorService.shutdownNow()
    }
}