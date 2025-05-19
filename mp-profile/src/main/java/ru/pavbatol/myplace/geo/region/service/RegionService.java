package ru.pavbatol.myplace.geo.region.service;

import org.springframework.data.domain.Slice;
import ru.pavbatol.myplace.geo.region.dto.RegionDto;

public interface RegionService {
    RegionDto create(RegionDto dto);

    RegionDto update(Long regionId, RegionDto dto);

    void delete(Long regionId);

    RegionDto getById(Long regionId);

    Slice<RegionDto> getAll(String nameStartWith, String lastSeenName, String lastSeenCountryName, int size);
}
