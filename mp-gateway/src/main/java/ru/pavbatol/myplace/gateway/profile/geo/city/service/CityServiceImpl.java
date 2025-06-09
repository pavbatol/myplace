package ru.pavbatol.myplace.gateway.profile.geo.city.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.api.ResponseHandler;
import ru.pavbatol.myplace.gateway.profile.geo.city.client.CityClient;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.city.CityDto;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityClient client;
    private final ResponseHandler responseHandler;

    @Override
    public ApiResponse<CityDto> create(CityDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.create(dto, headers);
        return responseHandler.processResponse(response, CityDto.class);
    }

    @Override
    public ApiResponse<CityDto> update(Long cityId, CityDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.update(cityId, dto, headers);
        return responseHandler.processResponse(response, CityDto.class);
    }

    @Override
    public ApiResponse<Void> delete(Long cityId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.delete(cityId, headers);
        return responseHandler.processResponse(response, Void.class);
    }

    @Override
    public ApiResponse<CityDto> getById(Long cityId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getById(cityId, headers);
        return responseHandler.processResponse(response, CityDto.class);
    }

    @Override
    public ApiResponse<SimpleSlice<CityDto>> getAll(String nameStartWith, String lastSeenName, Long lastSeenId, int size, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getAll(nameStartWith, lastSeenName, lastSeenId, size, headers);
        return responseHandler.processResponseSimpleSlice(response, CityDto.class);
    }
}
