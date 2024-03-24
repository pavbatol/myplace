package ru.pavbatol.myplace.geo.house.mapper;

import org.mapstruct.*;
import ru.pavbatol.myplace.geo.house.dto.HouseDto;
import ru.pavbatol.myplace.geo.house.model.House;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface HouseMapper {
    HouseDto toHouseDto(House entity);

    House toEntity(HouseDto dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    House updateEntity(@MappingTarget House entity, HouseDto dto);
}
