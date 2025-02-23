package ru.pavbatol.myplace.geo.house.dto;

import lombok.Value;
import ru.pavbatol.myplace.app.Util.Marker;
import ru.pavbatol.myplace.geo.IdentifiableGeo;
import ru.pavbatol.myplace.geo.street.dto.StreetDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Value
public class HouseDto implements IdentifiableGeo {
    @Null(groups = Marker.OnCreate.class)
    Long id;

    @NotNull(groups = Marker.OnCreate.class)
    StreetDto street;

    @NotNull(groups = Marker.OnCreate.class)
    @Size(max = 10)
    String number;

    @PositiveOrZero
    Double lat;

    @PositiveOrZero
    Double lon;
}
