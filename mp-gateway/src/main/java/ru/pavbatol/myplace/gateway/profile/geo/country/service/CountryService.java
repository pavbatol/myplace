package ru.pavbatol.myplace.gateway.profile.geo.country.service;

import org.springframework.http.HttpHeaders;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.country.CountryDto;

public interface CountryService {
    ApiResponse<CountryDto> create(CountryDto dto, HttpHeaders headers);

    ApiResponse<CountryDto> update(Long countryId, CountryDto dto, HttpHeaders headers);

    ApiResponse<Void> delete(Long countryId, HttpHeaders headers);

    ApiResponse<CountryDto> getById(Long countryId, HttpHeaders headers);

    ApiResponse<SimpleSlice<CountryDto>> getAll(String nameStartWith, String lastSeenName, int size, HttpHeaders headers);
}
