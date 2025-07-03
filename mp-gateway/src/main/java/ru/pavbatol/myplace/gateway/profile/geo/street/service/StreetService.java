package ru.pavbatol.myplace.gateway.profile.geo.street.service;

import org.springframework.http.HttpHeaders;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.street.StreetDto;

public interface StreetService {
    ApiResponse<StreetDto> create(StreetDto dto, HttpHeaders headers);

    ApiResponse<StreetDto> update(Long streetId, StreetDto dto, HttpHeaders headers);

    ApiResponse<Void> delete(Long streetId, HttpHeaders headers);

    ApiResponse<StreetDto> getById(Long streetId, HttpHeaders headers);

    ApiResponse<SimpleSlice<StreetDto>> getAll(String nameStartWith, String lastSeenName, Long lastSeenId, int size, HttpHeaders headers);
}
