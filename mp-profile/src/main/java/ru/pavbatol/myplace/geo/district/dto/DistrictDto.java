package ru.pavbatol.myplace.geo.district.dto;

import lombok.Value;
import ru.pavbatol.myplace.geo.region.dto.RegionDto;

@Value
public class DistrictDto {
    Long id;

    RegionDto region;

    String name;
}
