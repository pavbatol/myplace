package ru.pavbatol.myplace.gateway.profile.geo.street.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.api.ResponseHandler;
import ru.pavbatol.myplace.gateway.profile.geo.street.client.StreetClient;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.street.StreetDto;

@Service
@RequiredArgsConstructor
public class StreetServiceImpl implements StreetService {
    private final StreetClient client;
    private final ResponseHandler responseHandler;

    @Override
    public ApiResponse<StreetDto> create(StreetDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.create(dto, headers);
        return responseHandler.processResponse(response, StreetDto.class);
    }

    @Override
    public ApiResponse<StreetDto> update(Long streetId, StreetDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.update(streetId, dto, headers);
        return responseHandler.processResponse(response, StreetDto.class);
    }

    @Override
    public ApiResponse<Void> delete(Long streetId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.delete(streetId, headers);
        return responseHandler.processResponse(response, Void.class);
    }

    @Override
    public ApiResponse<StreetDto> getById(Long streetId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getById(streetId, headers);
        return responseHandler.processResponse(response, StreetDto.class);
    }

    @Override
    public ApiResponse<SimpleSlice<StreetDto>> getAll(String nameStartWith, String lastSeenName, Long lastSeenId, int size, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getAll(nameStartWith, lastSeenName, lastSeenId, size, headers);
        return responseHandler.processResponseSimpleSlice(response, StreetDto.class);
    }
}
