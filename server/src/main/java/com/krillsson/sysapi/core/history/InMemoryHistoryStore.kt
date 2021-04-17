package com.krillsson.sysapi.core.history

import com.krillsson.sysapi.core.domain.history.SystemHistoryEntry
import com.krillsson.sysapi.persistence.Store

class InMemoryHistoryStore : Store<List<SystemHistoryEntry>> {

    private val history: MutableList<SystemHistoryEntry> = mutableListOf()

    override fun write(content: List<SystemHistoryEntry>) {
        history.clear()
        history.addAll(content)
    }

    override fun read(): List<SystemHistoryEntry> {
        return history.toList()
    }

    override fun clear() {
        history.clear()
    }
}