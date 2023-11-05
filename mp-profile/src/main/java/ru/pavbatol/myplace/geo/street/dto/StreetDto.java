package ru.pavbatol.myplace.geo.street.dto;

import lombok.Value;
import ru.pavbatol.myplace.geo.city.dto.CityDto;

@Value
public class StreetDto {
    Long id;

    CityDto city;

    String name;
}
