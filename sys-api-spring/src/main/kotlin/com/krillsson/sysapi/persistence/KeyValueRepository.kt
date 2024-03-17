package com.krillsson.sysapi.persistence

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.stereotype.Component

@Component
class KeyValueRepository(private val keyValueStore: KeyValueStore)  {

    private lateinit var memory: MutableMap<String, String>

    fun put(key: String, value: String?) {
        if (value != null) {
            memory[key] = value
        } else {
            remove(key)
        }
        persist()
    }

    fun get(key: String): String? = memory[key]

    fun remove(key: String): Boolean {
        val removedKey = memory.remove(key)
        persist()
        return removedKey != null
    }

    @PostConstruct
    fun start() {
        restore()
    }

    @PreDestroy
    fun stop() {
        persist()
    }

    private fun persist() {
        keyValueStore.write(memory)
    }

    private fun restore() {
        memory = mutableMapOf()
        keyValueStore.read()?.forEach { k, v ->
            memory[k] = v
        }
    }
}