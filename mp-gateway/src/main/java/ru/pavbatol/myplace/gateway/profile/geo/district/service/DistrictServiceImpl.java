package ru.pavbatol.myplace.gateway.profile.geo.district.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.api.ResponseHandler;
import ru.pavbatol.myplace.gateway.profile.geo.district.client.DistrictClient;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.district.DistrictDto;

@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {
    private final DistrictClient client;
    private final ResponseHandler responseHandler;

    @Override
    public ApiResponse<DistrictDto> create(DistrictDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.create(dto, headers);
        return responseHandler.processResponse(response, DistrictDto.class);
    }

    @Override
    public ApiResponse<DistrictDto> update(Long districtId, DistrictDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.update(districtId, dto, headers);
        return responseHandler.processResponse(response, DistrictDto.class);
    }

    @Override
    public ApiResponse<Void> delete(Long districtId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.delete(districtId, headers);
        return responseHandler.processResponse(response, Void.class);
    }

    @Override
    public ApiResponse<DistrictDto> getById(Long districtId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getById(districtId, headers);
        return responseHandler.processResponse(response, DistrictDto.class);
    }

    @Override
    public ApiResponse<SimpleSlice<DistrictDto>> getAll(String nameStartWith, String lastSeenName, Long lastSeenId, int size, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getAll(nameStartWith, lastSeenName, lastSeenId, size, headers);
        return responseHandler.processResponseSimpleSlice(response, DistrictDto.class);
    }
}
