package com.krillsson.sysapi.core.domain.docker

enum class State {
    CREATED,
    RESTARTING,
    RUNNING,
    PAUSED,
    EXITED,
    DEAD,
    UNKNOWN;

    companion object {
        fun fromString(state: String): State {
            return when (state) {
                "created" -> CREATED
                "restarting" -> RESTARTING
                "running" -> RUNNING
                "paused" -> PAUSED
                "exited" -> EXITED
                "dead" -> DEAD
                else -> UNKNOWN
            }
        }
    }
}