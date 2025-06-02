package ru.pavbatol.myplace.geo.city.service;

import ru.pavbatol.myplace.geo.city.dto.CityDto;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;

public interface CityService {
    CityDto create(CityDto dto);

    CityDto update(Long cityId, CityDto dto);

    void delete(Long cityId);

    CityDto getById(Long cityId);

    SimpleSlice<CityDto> getAll(String nameStartWith, String lastSeenName, Long lastSeenId, int size);
}
