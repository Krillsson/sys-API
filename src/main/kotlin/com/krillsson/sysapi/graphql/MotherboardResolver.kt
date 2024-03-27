package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.motherboard.Motherboard
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component

@Component
class MotherboardResolver : GraphQLResolver<Motherboard> {
    fun getManufacturer(motherboard: Motherboard) = motherboard.computerSystem.manufacturer
    fun getModel(motherboard: Motherboard) = motherboard.computerSystem.model
    fun getSerialNumber(motherboard: Motherboard) = motherboard.computerSystem.serialNumber
    fun getFirmware(motherboard: Motherboard) = motherboard.computerSystem.firmware
}