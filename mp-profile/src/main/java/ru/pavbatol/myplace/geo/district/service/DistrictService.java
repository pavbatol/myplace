package ru.pavbatol.myplace.geo.district.service;

import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.district.DistrictDto;

public interface DistrictService {
    DistrictDto create(DistrictDto dto);

    DistrictDto update(Long districtId, DistrictDto dto);

    void delete(Long districtId);

    DistrictDto getById(Long districtId);

    SimpleSlice<DistrictDto> getAll(String nameStartWith, String lastSeenName, Long lastSeenId, int size);
}
