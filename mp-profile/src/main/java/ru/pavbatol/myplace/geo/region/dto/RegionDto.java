package ru.pavbatol.myplace.geo.region.dto;

import lombok.Value;
import ru.pavbatol.myplace.app.Util.Marker;
import ru.pavbatol.myplace.geo.country.dto.CountryDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Value
public class RegionDto {
    @Null(groups = Marker.OnCreate.class)
    Long id;

    @NotNull(groups = Marker.OnCreate.class)
    CountryDto country;

    @NotNull(groups = Marker.OnCreate.class)
    String name;
}
