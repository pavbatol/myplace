package ru.pavbatol.myplace.profile.dto;

import lombok.Value;
import ru.pavbatol.myplace.geo.city.dto.CityDto;
import ru.pavbatol.myplace.geo.country.dto.CountryDto;
import ru.pavbatol.myplace.geo.district.dto.DistrictDto;
import ru.pavbatol.myplace.geo.house.dto.HouseDto;
import ru.pavbatol.myplace.geo.region.dto.RegionDto;
import ru.pavbatol.myplace.geo.street.dto.StreetDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class ProfileDtoUpdateAddressRequest {
    @NotNull
    CountryDto country;

    @NotNull
    RegionDto region;

    @NotNull
    DistrictDto district;

    @NotNull
    CityDto city;

    @NotNull
    StreetDto street;

    @NotNull
    HouseDto house;

    @Size(max = 10)
    String apartment;
}
