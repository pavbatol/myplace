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

/**
 * Service implementation for Country operations.
 * Acts as an intermediary between controllers and the Country client,
 * handling requests and processing responses.
 */
@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryClient client;
    private final ResponseHandler responseHandler;

    /**
     * Creates a new Country.
     * @param dto Country data to create
     * @param headers HTTP headers for the request
     * @return ApiResponse with created Country data
     */
    @Override
    public ApiResponse<CountryDto> create(CountryDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.create(dto, headers);
        return responseHandler.processResponse(response, CountryDto.class);
    }

    /**
     * Updates an existing Country.
     * @param countryId ID of the Country to update
     * @param dto Updated Country data
     * @param headers HTTP headers for the request
     * @return ApiResponse with updated Country data
     */
    @Override
    public ApiResponse<CountryDto> update(Long countryId, CountryDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.update(countryId, dto, headers);
        return responseHandler.processResponse(response, CountryDto.class);
    }

    /**
     * Deletes a Country.
     * @param countryId ID of the Country to delete
     * @param headers HTTP headers for the request
     * @return ApiResponse with operation status
     */
    @Override
    public ApiResponse<Void> delete(Long countryId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.delete(countryId, headers);
        return responseHandler.processResponse(response, Void.class);
    }

    /**
     * Retrieves a Country by ID.
     * @param countryId ID of the Country to retrieve
     * @param headers HTTP headers for the request
     * @return ApiResponse with requested Country data
     */
    @Override
    public ApiResponse<CountryDto> getById(Long countryId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getById(countryId, headers);
        return responseHandler.processResponse(response, CountryDto.class);
    }

    /**
     * Retrieves paginated list of Countries with optional filtering.
     * @param nameStartWith Filter by name prefix (optional)
     * @param lastSeenName Pagination cursor (optional)
     * @param size Number of items per page
     * @param headers HTTP headers for the request
     * @return ApiResponse with paginated Country results
     */
    @Override
    public ApiResponse<SimpleSlice<CountryDto>> getAll(String nameStartWith, String lastSeenName, int size, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getAll(nameStartWith, lastSeenName, size, headers);
        return responseHandler.processResponseSimpleSlice(response, CountryDto.class);
    }
}
