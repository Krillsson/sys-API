package com.krillsson.sysapi.util

import io.dropwizard.lifecycle.Managed
import java.time.Duration
import java.util.*
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class Ticker(private val executorService: ScheduledExecutorService, measurementInterval: Int) : Managed {
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
            val error: Throwable
        ) : TickContainer
    }

    private val logger by logger()
    private val measurementInterval: Long = Duration.ofSeconds(measurementInterval.toLong()).seconds
    private val containers: MutableList<TickContainer> = ArrayList()


    fun register(listener: TickListener) {
        containers.add(TickContainer.Active(listener, UUID.randomUUID()))
    }

    fun unregister(tickListener: TickListener) {
        containers.removeIf { it.tickListener == tickListener }
    }

    @Throws(Exception::class)
    override fun start() {
        executorService.scheduleAtFixedRate({ execute() }, 1, measurementInterval, TimeUnit.SECONDS)
    }

    private fun execute() {
        val evictedContainers = mutableListOf<TickContainer.Evicted>()
        for (listener in containers) {
            try {
                when (listener) {
                    is TickContainer.Active -> listener.tickListener.onTick()
                    is TickContainer.Evicted -> {
                        logger.warn("Not executing evicted ${listener.tickListener::class.java.simpleName}/${listener.id} due to previous error ${listener.error::class.java.simpleName} ${listener.error.message}")
                    }
                }
            } catch (e: Throwable) {
                logger.error("Error while executing ticker. Evicting...", e)
                val evicted = TickContainer.Evicted(listener.tickListener, listener.id, e)
                evictedContainers += evicted
            }
        }
        evictedContainers.forEach { evicted ->
            containers.removeIf { it.id == evicted.id }
            containers.add(evicted)
        }
    }

    @Throws(Exception::class)
    override fun stop() {
        executorService.shutdownNow()
    }
}