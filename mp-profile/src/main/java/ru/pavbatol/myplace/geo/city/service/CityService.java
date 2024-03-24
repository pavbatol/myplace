package ru.pavbatol.myplace.geo.city.service;

import org.springframework.data.domain.Slice;
import ru.pavbatol.myplace.geo.city.dto.CityDto;

public interface CityService {
    CityDto create(CityDto dto);

    CityDto update(Long cityId, CityDto dto);

    void delete(Long cityId);

    CityDto getById(Long cityId);

    Slice<CityDto> getAll(String nameStartWith, int page, int size);
}
