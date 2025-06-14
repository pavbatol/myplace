package ru.pavbatol.myplace.geo.city.dto;

import lombok.Value;
import ru.pavbatol.myplace.app.util.Marker;
import ru.pavbatol.myplace.geo.common.IdentifiableGeo;
import ru.pavbatol.myplace.geo.district.dto.DistrictDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Value
public class CityDto implements IdentifiableGeo {

    @Null(groups = Marker.OnCreate.class)
    Long id;

    @NotNull(groups = Marker.OnCreate.class)
    DistrictDto district;

    @NotNull(groups = Marker.OnCreate.class)
    @Size(max = 150)
    String name;
}
