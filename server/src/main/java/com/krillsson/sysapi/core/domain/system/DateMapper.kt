package com.krillsson.sysapi.core.domain.system

import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface DateMapper {
    fun map(localDateTime: OffsetDateTime): String? {
        return localDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    companion object {
        val INSTANCE = Mappers.getMapper(DateMapper::class.java)
    }
}