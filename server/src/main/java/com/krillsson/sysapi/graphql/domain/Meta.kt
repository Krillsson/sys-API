package com.krillsson.sysapi.graphql.domain

data class Meta(
    val version: String,
    val buildDate: String,
    val processId: Int,
    val endpoints: List<String>
)