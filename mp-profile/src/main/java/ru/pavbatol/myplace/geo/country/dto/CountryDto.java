package ru.pavbatol.myplace.geo.country.dto;

import lombok.Value;
import ru.pavbatol.myplace.app.Util.Marker;

import javax.validation.constraints.Null;

@Value
public class CountryDto {
    @Null(groups = Marker.OnCreate.class)
    Long id;

    @Null(groups = Marker.OnCreate.class)
    String code;

    @Null(groups = Marker.OnCreate.class)
    String name;
}
