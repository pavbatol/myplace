package ru.pavbatol.myplace.geo.street.dto;

import lombok.Value;
import ru.pavbatol.myplace.app.Util.Marker;
import ru.pavbatol.myplace.geo.Identifiable;
import ru.pavbatol.myplace.geo.city.dto.CityDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Value
public class StreetDto implements Identifiable {
    @Null(groups = Marker.OnCreate.class)
    Long id;

    @NotNull(groups = Marker.OnCreate.class)
    CityDto city;

    @NotNull(groups = Marker.OnCreate.class)
    @Size(max = 150)
    String name;
}
