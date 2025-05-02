package ru.pavbatol.myplace.shared.dto.profile.geo.country;

import lombok.Value;
import ru.pavbatol.myplace.shared.dto.profile.geo.IdentifiableGeo;
import ru.pavbatol.myplace.shared.util.Marker;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Value
public class CountryDto implements IdentifiableGeo {
    @Null(groups = Marker.OnCreate.class)
    Long id;

    @NotNull(groups = Marker.OnCreate.class)
    String code;

    @NotNull(groups = Marker.OnCreate.class)
    @Size(max = 150)
    String name;
}
