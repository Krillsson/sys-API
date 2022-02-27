package com.krillsson.sysapi.persistence

import io.dropwizard.lifecycle.Managed

class KeyValueRepository(private val keyValueStore: Store<Map<String, String>>) : Managed {

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

    override fun start() {
        restore()
    }

    override fun stop() {
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