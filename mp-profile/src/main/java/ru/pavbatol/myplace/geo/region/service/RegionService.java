package ru.pavbatol.myplace.geo.region.service;

import ru.pavbatol.myplace.app.pagination.SliceDto;
import ru.pavbatol.myplace.geo.region.dto.RegionDto;

public interface RegionService {
    RegionDto create(RegionDto dto);

    RegionDto update(Long regionId, RegionDto dto);

    void delete(Long regionId);

    RegionDto getById(Long regionId);

    SliceDto<RegionDto> getAll(String nameStartWith, String lastSeenName, String lastSeenCountryName, int size);
}
