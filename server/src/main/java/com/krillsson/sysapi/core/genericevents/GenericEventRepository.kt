package com.krillsson.sysapi.core.genericevents

import com.krillsson.sysapi.persistence.Store
import java.util.*

class GenericEventRepository(private val store: Store<List<GenericEvent>>) {

    fun read(): List<GenericEvent> {
        return store.read().orEmpty()
    }

    fun write(value: List<GenericEvent>) {
        store.write(value)
    }

    fun add(event: GenericEvent) {
        update {
            val list = it.toMutableList()
            list.add(event)
            list
        }
    }

    fun update(event: GenericEvent) {
        update {
            val list = it.toMutableList()
            list.removeIf { toBeRemoved -> toBeRemoved.id == event.id }
            list.add(event)
            list
        }
    }

    fun removeById(id: UUID): Boolean {
        var result = false
        update {
            val list = it.toMutableList()
            result = list.removeIf { event -> event.id == id }
            list
        }
        return result
    }

    private fun update(action: (List<GenericEvent>) -> List<GenericEvent>) {
        val previousValue = read()
        val newValue = action(previousValue)
        write(newValue)
    }


}