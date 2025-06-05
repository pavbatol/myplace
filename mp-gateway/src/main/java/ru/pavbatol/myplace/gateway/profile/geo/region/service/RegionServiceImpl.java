package ru.pavbatol.myplace.gateway.profile.geo.region.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.api.ResponseHandler;
import ru.pavbatol.myplace.gateway.profile.geo.region.client.RegionClient;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.region.RegionDto;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {
    private final RegionClient client;
    private final ResponseHandler responseHandler;

    @Override
    public ApiResponse<RegionDto> create(RegionDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.create(dto, headers);
        return responseHandler.processResponse(response, RegionDto.class);
    }

    @Override
    public ApiResponse<RegionDto> update(Long regionId, RegionDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.update(regionId, dto, headers);
        return responseHandler.processResponse(response, RegionDto.class);
    }

    @Override
    public ApiResponse<Void> delete(Long regionId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.delete(regionId, headers);
        return responseHandler.processResponse(response, Void.class);
    }

    @Override
    public ApiResponse<RegionDto> getById(Long regionId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getById(regionId, headers);
        return responseHandler.processResponse(response, RegionDto.class);
    }

    @Override
    public ApiResponse<SimpleSlice<RegionDto>> getAll(String nameStartWith, String lastSeenName, String lastSeenCountryName, int size, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getAll(nameStartWith, lastSeenName, lastSeenCountryName, size, headers);
        return responseHandler.processResponseSimpleSlice(response, RegionDto.class);
    }
}
