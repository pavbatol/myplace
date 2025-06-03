package ru.pavbatol.myplace.gateway.profile.geo.country.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.api.ResponseHandler;
import ru.pavbatol.myplace.gateway.profile.geo.country.client.CountryClient;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.country.CountryDto;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryClient client;
    private final ResponseHandler responseHandler;

    @Override
    public ApiResponse<CountryDto> create(CountryDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.create(dto, headers);
        return responseHandler.processResponse(response, CountryDto.class);
    }

    @Override
    public ApiResponse<CountryDto> update(Long countryId, CountryDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.update(countryId, dto, headers);
        return responseHandler.processResponse(response, CountryDto.class);
    }

    @Override
    public ApiResponse<Void> delete(Long countryId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.delete(countryId, headers);
        return responseHandler.processResponse(response, Void.class);
    }

    @Override
    public ApiResponse<CountryDto> getById(Long countryId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getById(countryId, headers);
        return responseHandler.processResponse(response, CountryDto.class);
    }

    @Override
    public ApiResponse<SimpleSlice<CountryDto>> getAll(String nameStartWith, String lastSeenName, int size, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getAll(nameStartWith, lastSeenName, size, headers);
        return responseHandler.processResponseSimpleSlice(response, CountryDto.class);
    }
}
