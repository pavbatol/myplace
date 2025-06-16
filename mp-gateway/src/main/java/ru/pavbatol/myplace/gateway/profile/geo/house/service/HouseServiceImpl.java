package ru.pavbatol.myplace.gateway.profile.geo.house.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.api.ResponseHandler;
import ru.pavbatol.myplace.gateway.profile.geo.house.client.HouseClient;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.house.HouseDto;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {
    private final HouseClient client;
    private final ResponseHandler responseHandler;

    @Override
    public ApiResponse<HouseDto> create(HouseDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.create(dto, headers);
        return responseHandler.processResponse(response, HouseDto.class);
    }

    @Override
    public ApiResponse<HouseDto> update(Long houseId, HouseDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.update(houseId, dto, headers);
        return responseHandler.processResponse(response, HouseDto.class);
    }

    @Override
    public ApiResponse<Void> delete(Long houseId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.delete(houseId, headers);
        return responseHandler.processResponse(response, Void.class);
    }

    @Override
    public ApiResponse<HouseDto> getById(Long houseId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getById(houseId, headers);
        return responseHandler.processResponse(response, HouseDto.class);
    }

    @Override
    public ApiResponse<SimpleSlice<HouseDto>> getAll(String numberStartWith, String lastSeenNumber, Long lastSeenId, int size, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getAll(numberStartWith, lastSeenNumber, lastSeenId, size, headers);
        return responseHandler.processResponseSimpleSlice(response, HouseDto.class);
    }
}
