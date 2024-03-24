package ru.pavbatol.myplace.geo.street.mapper;

import org.mapstruct.*;
import ru.pavbatol.myplace.geo.street.dto.StreetDto;
import ru.pavbatol.myplace.geo.street.model.Street;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface StreetMapper {
    StreetDto toStreetDto(Street entity);

    Street toEntity(StreetDto dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Street updateEntity(@MappingTarget Street entity, StreetDto dto);
}
