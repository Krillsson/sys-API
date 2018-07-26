package com.krillsson.sysapi.core.domain.system;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface DateMapper {
    DateMapper INSTANCE = Mappers.getMapper(DateMapper.class);

    default String map(LocalDateTime localDateTime) {
        return localDateTime.toString();
    }

}
