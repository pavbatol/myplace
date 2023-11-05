package ru.pavbatol.myplace.geo.city.dto;

import lombok.Value;
import ru.pavbatol.myplace.geo.district.dto.DistrictDto;

@Value
public class CityDto {
    Long id;

    DistrictDto district;

    String name;
}
