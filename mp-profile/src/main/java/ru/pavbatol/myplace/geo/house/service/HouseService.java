package ru.pavbatol.myplace.geo.house.service;

import ru.pavbatol.myplace.geo.house.dto.HouseDto;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;

public interface HouseService {
    HouseDto create(HouseDto dto);

    HouseDto update(Long houseId, HouseDto dto);

    void delete(Long houseId);

    HouseDto getById(Long houseId);

    SimpleSlice<HouseDto> getAll(String numberStartWith, String lastSeenNumber, Long lastSeenId, int size);
}
