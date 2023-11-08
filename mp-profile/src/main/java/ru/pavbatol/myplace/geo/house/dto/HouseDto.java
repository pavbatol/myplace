package ru.pavbatol.myplace.geo.house.dto;

import lombok.Value;
import ru.pavbatol.myplace.app.Util.Marker;
import ru.pavbatol.myplace.geo.Identifiable;
import ru.pavbatol.myplace.geo.street.dto.StreetDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Value
public class HouseDto implements Identifiable {
    @Null(groups = Marker.OnCreate.class)
    Long id;

    @NotNull(groups = Marker.OnCreate.class)
    StreetDto street;

    @NotNull(groups = Marker.OnCreate.class)
    @Size(max = 10)
    String number;

    double lat;

    double lon;
}
