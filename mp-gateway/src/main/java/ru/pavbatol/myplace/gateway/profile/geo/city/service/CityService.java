package ru.pavbatol.myplace.gateway.profile.geo.city.service;

import org.springframework.http.HttpHeaders;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.city.CityDto;


public interface CityService {
    ApiResponse<CityDto> create(CityDto dto, HttpHeaders headers);

    ApiResponse<CityDto> update(Long cityId, CityDto dto, HttpHeaders headers);

    ApiResponse<Void> delete(Long cityId, HttpHeaders headers);

    ApiResponse<CityDto> getById(Long cityId, HttpHeaders headers);

    ApiResponse<SimpleSlice<CityDto>> getAll(String nameStartWith, String lastSeenName, Long lastSeenId, int size, HttpHeaders headers);
}
