package ru.pavbatol.myplace.geo.house.dto;

import lombok.Value;
import ru.pavbatol.myplace.app.Util.Marker;
import ru.pavbatol.myplace.geo.street.dto.StreetDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Value
public class HouseDto {
    @Null(groups = Marker.OnCreate.class)
    Long id;

    @NotNull(groups = Marker.OnCreate.class)
    StreetDto street;

    @NotNull(groups = Marker.OnCreate.class)
    String number;

    double lat;

    double lon;
}
