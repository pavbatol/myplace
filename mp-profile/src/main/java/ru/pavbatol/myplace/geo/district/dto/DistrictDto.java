package ru.pavbatol.myplace.geo.district.dto;

import lombok.Value;
import ru.pavbatol.myplace.app.Util.Marker;
import ru.pavbatol.myplace.geo.region.dto.RegionDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Value
public class DistrictDto {
    @Null(groups = Marker.OnCreate.class)
    @NotNull(groups = Marker.OnUpdate.class)
    Long id;

    @NotNull(groups = Marker.OnCreate.class)
    RegionDto region;

    @NotNull(groups = Marker.OnCreate.class)
    String name;
}
