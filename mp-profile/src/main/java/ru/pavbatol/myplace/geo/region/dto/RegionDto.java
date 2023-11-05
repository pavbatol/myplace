package ru.pavbatol.myplace.geo.region.dto;

import lombok.Value;
import ru.pavbatol.myplace.geo.country.dto.CountryDto;

@Value
public class RegionDto {
    Long id;

    CountryDto country;

    String name;
}
