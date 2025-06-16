package ru.pavbatol.myplace.gateway.profile.geo.house.service;

import org.springframework.http.HttpHeaders;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.house.HouseDto;

public interface HouseService {
    ApiResponse<HouseDto> create(HouseDto dto, HttpHeaders headers);

    ApiResponse<HouseDto> update(Long houseId, HouseDto dto, HttpHeaders headers);

    ApiResponse<Void> delete(Long houseId, HttpHeaders headers);

    ApiResponse<HouseDto> getById(Long houseId, HttpHeaders headers);

    ApiResponse<SimpleSlice<HouseDto>> getAll(String numberStartWith, String lastSeenNumber, Long lastSeenId, int size, HttpHeaders headers);
}
