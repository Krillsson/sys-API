package com.krillsson.sysapi.util

import io.dropwizard.lifecycle.Managed
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class Ticker(
    private val executorService: ScheduledExecutorService,
    private val measurementIntervalSeconds: Long,
    private val retryIntervalSeconds: Long
) : Managed {
    interface TickListener {
        fun onTick()
    }

    sealed interface TickContainer {
        val tickListener: TickListener
        val id: UUID

        data class Active(
            override val tickListener: TickListener,
            override val id: UUID
        ) : TickContainer

        data class Evicted(
            override val tickListener: TickListener,
            override val id: UUID,
            val evictedAt: Instant,
            val error: Throwable
        ) : TickContainer
    }

    private val logger by logger()
    private val containers: MutableList<TickContainer.Active> = mutableListOf()
    private val evictedContainers: MutableList<TickContainer.Evicted> = mutableListOf()


    fun register(listener: TickListener) {
        containers.add(TickContainer.Active(listener, UUID.randomUUID()))
    }

    fun unregister(tickListener: TickListener) {
        containers.removeIf { it.tickListener == tickListener }
    }

    @Throws(Exception::class)
    override fun start() {
        executorService.scheduleAtFixedRate({ execute() }, 1, measurementIntervalSeconds, TimeUnit.SECONDS)
    }

    private fun execute() {
        retryEvicted()
        val evictedContainers = mutableListOf<TickContainer.Evicted>()
        for (listener in containers) {
            try {
                listener.tickListener.onTick()
            } catch (e: Throwable) {
                logger.error(
                    "Error while executing ticker ${listener.tickListener::class.java.simpleName}/${listener.id} Evicting...",
                    e
                )
                val evicted = TickContainer.Evicted(listener.tickListener, listener.id, Instant.now(), e)
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
                logger.info("Retrying ticker ${evicted.tickListener::class.java.simpleName}/${evicted.id} after ${retryIntervalSeconds}s")
                evictedContainers.removeIf { it.id == evicted.id }
                containers.add(
                    TickContainer.Active(evicted.tickListener, evicted.id)
                )
            }
        }
    }

    private fun evict(containersToEvict: List<TickContainer.Evicted>) {
        containersToEvict.forEach { evicted ->
            containers.removeIf { it.id == evicted.id }
            evictedContainers.add(evicted)
        }
    }

    @Throws(Exception::class)
    override fun stop() {
        executorService.shutdownNow()
    }
}