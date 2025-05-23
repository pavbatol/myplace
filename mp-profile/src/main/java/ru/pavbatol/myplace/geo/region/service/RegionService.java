package ru.pavbatol.myplace.geo.region.service;

import ru.pavbatol.myplace.geo.region.dto.RegionDto;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;

public interface RegionService {
    RegionDto create(RegionDto dto);

    RegionDto update(Long regionId, RegionDto dto);

    void delete(Long regionId);

    RegionDto getById(Long regionId);

    SimpleSlice<RegionDto> getAll(String nameStartWith, String lastSeenName, String lastSeenCountryName, int size);
}
