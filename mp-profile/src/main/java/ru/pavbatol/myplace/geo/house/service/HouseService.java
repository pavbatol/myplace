package ru.pavbatol.myplace.geo.house.service;

import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.house.HouseDto;

public interface HouseService {
    HouseDto create(HouseDto dto);

    HouseDto update(Long houseId, HouseDto dto);

    void delete(Long houseId);

    HouseDto getById(Long houseId);

    SimpleSlice<HouseDto> getAll(String numberStartWith, String lastSeenNumber, Long lastSeenId, int size);
}
