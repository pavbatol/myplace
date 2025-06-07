package ru.pavbatol.myplace.shared.dto.profile.geo.district;

import lombok.Value;
import ru.pavbatol.myplace.shared.dto.profile.geo.IdentifiableGeo;
import ru.pavbatol.myplace.shared.dto.profile.geo.region.RegionDto;
import ru.pavbatol.myplace.shared.util.Marker;

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
