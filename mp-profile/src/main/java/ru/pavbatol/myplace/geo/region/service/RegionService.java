package ru.pavbatol.myplace.geo.region.service;

import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.region.RegionDto;

public interface RegionService {
    RegionDto create(RegionDto dto);

    RegionDto update(Long regionId, RegionDto dto);

    void delete(Long regionId);

    RegionDto getById(Long regionId);

    SimpleSlice<RegionDto> getAll(String nameStartWith, String lastSeenName, String lastSeenCountryName, int size);
}
