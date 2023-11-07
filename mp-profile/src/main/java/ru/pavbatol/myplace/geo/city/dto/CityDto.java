package ru.pavbatol.myplace.geo.city.dto;

import lombok.Value;
import ru.pavbatol.myplace.app.Util.Marker;
import ru.pavbatol.myplace.geo.district.dto.DistrictDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Value
public class CityDto {

    @Null(groups = Marker.OnCreate.class)
    Long id;

    @NotNull(groups = Marker.OnCreate.class)
    DistrictDto district;

    @NotNull(groups = Marker.OnCreate.class)
    String name;
}
