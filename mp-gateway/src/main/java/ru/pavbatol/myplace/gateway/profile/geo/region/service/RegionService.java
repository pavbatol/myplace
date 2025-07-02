package ru.pavbatol.myplace.gateway.profile.geo.region.service;

import org.springframework.http.HttpHeaders;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.region.RegionDto;

public interface RegionService {
    ApiResponse<RegionDto> create(RegionDto dto, HttpHeaders headers);

    ApiResponse<RegionDto> update(Long regionId, RegionDto dto, HttpHeaders headers);

    ApiResponse<Void> delete(Long regionId, HttpHeaders headers);

    ApiResponse<RegionDto> getById(Long regionId, HttpHeaders headers);

    ApiResponse<SimpleSlice<RegionDto>> getAll(String nameStartWith, String lastSeenName, String lastSeenCountryName, int size, HttpHeaders headers);
}
