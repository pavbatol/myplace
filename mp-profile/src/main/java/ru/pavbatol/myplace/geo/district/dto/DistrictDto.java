package ru.pavbatol.myplace.geo.district.dto;

import lombok.Value;
import ru.pavbatol.myplace.app.Util.Marker;
import ru.pavbatol.myplace.geo.IdentifiableGeo;
import ru.pavbatol.myplace.geo.region.dto.RegionDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Value
public class DistrictDto implements IdentifiableGeo {
    @Null(groups = Marker.OnCreate.class)
    Long id;

    @NotNull(groups = Marker.OnCreate.class)
    RegionDto region;

    @NotNull(groups = Marker.OnCreate.class)
    @Size(max = 255)
    String name;
}
