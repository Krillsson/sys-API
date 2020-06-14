package com.krillsson.sysapi.core.domain.metadata;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MetaMapper {
    MetaMapper INSTANCE = Mappers.getMapper(MetaMapper.class);

    com.krillsson.sysapi.dto.metadata.Meta map(com.krillsson.sysapi.core.domain.metadata.Meta value);
}
