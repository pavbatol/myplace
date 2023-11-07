package ru.pavbatol.myplace.geo.city.mapper;

import org.mapstruct.*;
import ru.pavbatol.myplace.geo.city.dto.CityDto;
import ru.pavbatol.myplace.geo.city.model.City;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CityMapper {
    CityDto toCityDto(City city);

    City toEntity(CityDto dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    City updateEntity(@MappingTarget City entity, CityDto dto);
}
