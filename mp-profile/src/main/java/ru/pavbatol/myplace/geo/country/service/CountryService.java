package ru.pavbatol.myplace.geo.country.service;

import org.springframework.data.domain.Slice;
import ru.pavbatol.myplace.geo.country.dto.CountryDto;

public interface CountryService {
    CountryDto create(CountryDto dto);

    CountryDto update(Long countryId, CountryDto dto);

    void delete(Long countryId);

    CountryDto getById(Long countryId);

    Slice<CountryDto> getAll(String nameStartWith, String lastSeenName, int size);
}
