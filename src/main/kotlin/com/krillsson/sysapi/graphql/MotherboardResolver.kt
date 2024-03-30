package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.motherboard.Motherboard
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class MotherboardResolver {
    @SchemaMapping
    fun manufacturer(motherboard: Motherboard) = motherboard.computerSystem.manufacturer

    @SchemaMapping
    fun model(motherboard: Motherboard) = motherboard.computerSystem.model

    @SchemaMapping
    fun serialNumber(motherboard: Motherboard) = motherboard.computerSystem.serialNumber

    @SchemaMapping
    fun firmware(motherboard: Motherboard) = motherboard.computerSystem.firmware
}