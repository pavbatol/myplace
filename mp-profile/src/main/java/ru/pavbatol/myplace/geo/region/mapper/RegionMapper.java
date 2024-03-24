package ru.pavbatol.myplace.geo.region.mapper;

import org.mapstruct.*;
import ru.pavbatol.myplace.geo.region.dto.RegionDto;
import ru.pavbatol.myplace.geo.region.model.Region;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RegionMapper {
    RegionDto toRegionDto(Region entity);

    Region toEntity(RegionDto dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Region updateEntity(@MappingTarget Region entity, RegionDto dto);
}
