package ru.pavbatol.myplace.geo.city.service;

import ru.pavbatol.myplace.geo.city.dto.CityDto;
import ru.pavbatol.myplace.geo.city.model.City;

public interface CityService {
    CityDto create(City dto);

    CityDto update(Long cityId, CityDto dto);

    void delete(Long cityId);

    CityDto getById(Long cityId);
}
