package com.krillsson.sysapi.graphql.domain

import com.krillsson.sysapi.logaccess.LogMessage


object LogFileAccess

data class LogMessageEdge(
    val cursor: String,
    val node: LogMessage
)

data class PageInfo(
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val startCursor: String?,
    val endCursor: String?
)

data class LogMessageConnection(
    val edges: List<LogMessageEdge>,
    val pageInfo: PageInfo
)