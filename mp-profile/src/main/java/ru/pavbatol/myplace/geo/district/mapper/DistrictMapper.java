package ru.pavbatol.myplace.geo.district.mapper;

import org.mapstruct.*;
import ru.pavbatol.myplace.geo.district.dto.DistrictDto;
import ru.pavbatol.myplace.geo.district.model.District;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DistrictMapper {
    DistrictDto toDistrictDto(District entity);

    District toEntity(DistrictDto dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    District updateEntity(@MappingTarget District entity, DistrictDto dto);
}
