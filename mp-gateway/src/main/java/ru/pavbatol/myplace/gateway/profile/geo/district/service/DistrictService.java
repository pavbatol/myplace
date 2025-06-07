package ru.pavbatol.myplace.gateway.profile.geo.district.service;

import org.springframework.http.HttpHeaders;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.district.DistrictDto;

public interface DistrictService {
    ApiResponse<DistrictDto> create(DistrictDto dto, HttpHeaders headers);

    ApiResponse<DistrictDto> update(Long districtId, DistrictDto dto, HttpHeaders headers);

    ApiResponse<Void> delete(Long districtId, HttpHeaders headers);

    ApiResponse<DistrictDto> getById(Long districtId, HttpHeaders headers);

    ApiResponse<SimpleSlice<DistrictDto>> getAll(String nameStartWith, String lastSeenName, Long lastSeenId, int size, HttpHeaders headers);
}
