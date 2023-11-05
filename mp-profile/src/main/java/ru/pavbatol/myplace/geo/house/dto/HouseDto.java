package ru.pavbatol.myplace.geo.house.dto;

import lombok.Value;
import ru.pavbatol.myplace.geo.street.dto.StreetDto;

@Value
public class HouseDto {
    Long id;

    StreetDto street;

    String number;

    double lat;

    double lon;
}
