package ru.pavbatol.myplace.shared.dto.profile.geo.house;

import lombok.Value;
import ru.pavbatol.myplace.shared.dto.profile.geo.IdentifiableGeo;
import ru.pavbatol.myplace.shared.dto.profile.geo.street.StreetDto;
import ru.pavbatol.myplace.shared.util.Marker;

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
