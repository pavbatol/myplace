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

/**
 * Service implementation for City operations.
 * Handles business logic for City management and delegates to CityClient.
 */
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityClient client;
    private final ResponseHandler responseHandler;

    /**
     * Creates a new City.
     *
     * @param dto     City data to create
     * @param headers HTTP headers for the request
     * @return API response with created City
     */
    @Override
    public ApiResponse<CityDto> create(CityDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.create(dto, headers);
        return responseHandler.processResponse(response, CityDto.class);
    }

    /**
     * Updates an existing City.
     *
     * @param cityId  ID of City to update
     * @param dto     Updated City data
     * @param headers HTTP headers for the request
     * @return API response with updated City
     */
    @Override
    public ApiResponse<CityDto> update(Long cityId, CityDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.update(cityId, dto, headers);
        return responseHandler.processResponse(response, CityDto.class);
    }

    /**
     * Deletes a City.
     *
     * @param cityId  ID of City to delete
     * @param headers HTTP headers for the request
     * @return API response with operation status
     */
    @Override
    public ApiResponse<Void> delete(Long cityId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.delete(cityId, headers);
        return responseHandler.processResponse(response, Void.class);
    }

    /**
     * Retrieves a City by ID.
     *
     * @param cityId  ID of City to retrieve
     * @param headers HTTP headers for the request
     * @return API response with City data
     */
    @Override
    public ApiResponse<CityDto> getById(Long cityId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getById(cityId, headers);
        return responseHandler.processResponse(response, CityDto.class);
    }

    /**
     * Retrieves paginated list of Cities with optional filters.
     *
     * @param nameStartWith Filter by name prefix (optional)
     * @param lastSeenName  Pagination cursor by name (optional)
     * @param lastSeenId    Pagination cursor by ID (optional)
     * @param size          Number of items per page
     * @param headers       HTTP headers for the request
     * @return API response with paginated City results
     */
    @Override
    public ApiResponse<SimpleSlice<CityDto>> getAll(String nameStartWith, String lastSeenName, Long lastSeenId, int size, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getAll(nameStartWith, lastSeenName, lastSeenId, size, headers);
        return responseHandler.processResponseSimpleSlice(response, CityDto.class);
    }
}
