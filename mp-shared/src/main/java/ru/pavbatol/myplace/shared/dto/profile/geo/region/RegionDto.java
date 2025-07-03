package ru.pavbatol.myplace.shared.dto.profile.geo.region;

import lombok.Value;
import ru.pavbatol.myplace.shared.dto.profile.geo.IdentifiableGeo;
import ru.pavbatol.myplace.shared.dto.profile.geo.country.CountryDto;
import ru.pavbatol.myplace.shared.util.Marker;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Value
public class RegionDto implements IdentifiableGeo {
    @Null(groups = Marker.OnCreate.class)
    Long id;

    @NotNull(groups = Marker.OnCreate.class)
    CountryDto country;

    @NotNull(groups = Marker.OnCreate.class)
    @Size(max = 255)
    String name;
}
