package com.krillsson.sysapi.persistence

import io.dropwizard.lifecycle.Managed


fun <T> JsonFile<T>.memoryBacked() = MemoryBackedStore(this)
class MemoryBackedStore<T>(private val backedStore: JsonFile<T>) : Store<T>, Managed {

    private var memory: T? = null
    override fun write(content: T) {
        memory = content
        persist()
    }

    override fun read(): T? {
        return memory
    }

    override fun clear() {
        memory = null
        backedStore.clear()
    }

    override fun start() {
        restore()
    }

    override fun stop() {
        persist()
    }

    private fun persist() {
        memory?.let { backedStore.write(it) }
    }

    private fun restore() {
        memory = backedStore.read()
    }

}