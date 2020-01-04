package com.krillsson.sysapi.graphql.domain

enum class ProcessSortMethod {
    CPU,
    MEMORY,
    OLDEST,
    NEWEST,
    PID,
    PARENTPID,
    NAME
}