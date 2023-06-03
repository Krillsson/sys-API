package com.krillsson.sysapi.core.genericevents

import com.krillsson.sysapi.persistence.Store
import java.util.*

class GenericEventRepository(private val store: Store<List<GenericEventStore.StoredGenericEvent>>) {

    fun read(): List<GenericEvent> {
        return store.read().orEmpty().map { it.asDomain() }
    }

    fun write(value: List<GenericEvent>) {
        store.write(value.map { it.asStored() })
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

    private fun GenericEvent.asStored(): GenericEventStore.StoredGenericEvent {
        return when (this) {
            is GenericEvent.MonitoredItemMissing -> GenericEventStore.StoredGenericEvent.MonitoredItemMissing(
                id = id,
                dateTime = dateTime,
                monitorType = monitorType,
                monitorId = monitorId,
                monitoredItemId = monitoredItemId
            )

            is GenericEvent.UpdateAvailable -> GenericEventStore.StoredGenericEvent.UpdateAvailable(
                id = id,
                dateTime = dateTime,
                currentVersion = currentVersion,
                newVersion = newVersion,
                changeLogMarkdown = changeLogMarkdown,
                downloadUrl = downloadUrl,
                publishDate = publishDate
            )
        }
    }

    private fun GenericEventStore.StoredGenericEvent.asDomain(): GenericEvent {
        return when (this) {
            is GenericEventStore.StoredGenericEvent.MonitoredItemMissing -> GenericEvent.MonitoredItemMissing(
                id = id,
                dateTime = dateTime,
                monitorType = monitorType,
                monitorId = monitorId,
                monitoredItemId = monitoredItemId
            )

            is GenericEventStore.StoredGenericEvent.UpdateAvailable -> GenericEvent.UpdateAvailable(
                id = id,
                dateTime = dateTime,
                currentVersion = currentVersion,
                newVersion = newVersion,
                changeLogMarkdown = changeLogMarkdown,
                downloadUrl = downloadUrl,
                publishDate = publishDate
            )
        }
    }
}