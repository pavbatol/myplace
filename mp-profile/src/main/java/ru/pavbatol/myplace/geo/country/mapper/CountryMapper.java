package ru.pavbatol.myplace.geo.country.mapper;

import org.mapstruct.*;
import ru.pavbatol.myplace.geo.country.model.Country;
import ru.pavbatol.myplace.shared.dto.profile.geo.country.CountryDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CountryMapper {
    CountryDto toCountryDto(Country entity);

    Country toEntity(CountryDto dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Country updateEntity(@MappingTarget Country entity, CountryDto dto);
}
