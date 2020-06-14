package com.krillsson.sysapi.core.domain.metadata

import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface MetaMapper {
    fun map(value: Meta): com.krillsson.sysapi.dto.metadata.Meta

    companion object {
        @kotlin.jvm.JvmField
        val INSTANCE = Mappers.getMapper(MetaMapper::class.java)
    }
}