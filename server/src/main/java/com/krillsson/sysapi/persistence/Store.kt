package com.krillsson.sysapi.persistence

interface Store<T> {
    fun update(action: (T?) -> T) {
        val previousValue = read()
        val newValue = action(previousValue)
        write(newValue)
    }
    fun write(content: T)
    fun read(): T?
    fun clear()
}