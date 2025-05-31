package ru.pavbatol.myplace.geo.country.service;

import ru.pavbatol.myplace.geo.country.dto.CountryDto;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;

public interface CountryService {
    CountryDto create(CountryDto dto);

    CountryDto update(Long countryId, CountryDto dto);

    void delete(Long countryId);

    CountryDto getById(Long countryId);

    SimpleSlice<CountryDto> getAll(String nameStartWith, String lastSeenName, int size);
}
