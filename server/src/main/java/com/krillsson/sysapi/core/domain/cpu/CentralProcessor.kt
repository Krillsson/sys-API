package com.krillsson.sysapi.core.domain.cpu

class CentralProcessor(
    val logicalProcessorCount: Int,
    val physicalProcessorCount: Int,
    val name: String?,
    val identifier: String?,
    val family: String?,
    val vendor: String?,
    val vendorFreq: Long,
    val model: String?,
    val stepping: String?,
    val cpu64bit: Boolean
)