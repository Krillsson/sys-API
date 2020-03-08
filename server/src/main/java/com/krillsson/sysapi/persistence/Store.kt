package com.krillsson.sysapi.persistence

interface Store<T> {
    fun write(content: T)
    fun read(): T?
    fun clear()
}