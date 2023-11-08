package ru.pavbatol.myplace.profile.dto;

import lombok.Value;
import ru.pavbatol.myplace.geo.house.dto.HouseDto;

@Value
public class ProfileDtoUpdateAddressResponse {
    HouseDto house;
    String apartment;
}
