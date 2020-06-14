package com.krillsson.sysapi.core.domain.cpu

class CoreLoad(
    val user: Double,
    val nice: Double,
    val sys: Double,
    val idle: Double,
    val ioWait: Double,
    val irq: Double,
    val softIrq: Double,
    val steal: Double
) 