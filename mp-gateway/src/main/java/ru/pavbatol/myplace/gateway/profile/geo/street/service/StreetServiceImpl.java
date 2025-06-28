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

/**
 * Service implementation for Street operations.
 * Handles business logic and communicates with Street client.
 */
@Service
@RequiredArgsConstructor
public class StreetServiceImpl implements StreetService {
    private final StreetClient client;
    private final ResponseHandler responseHandler;

    /**
     * Creates a new Street.
     *
     * @param dto     Street data to create
     * @param headers HTTP headers for authentication
     * @return API response with created Street
     */
    @Override
    public ApiResponse<StreetDto> create(StreetDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.create(dto, headers);
        return responseHandler.processResponse(response, StreetDto.class);
    }

    /**
     * Updates an existing Street.
     *
     * @param streetId ID of Street to update
     * @param dto      Updated Street data
     * @param headers  HTTP headers for authentication
     * @return API response with updated Street
     */
    @Override
    public ApiResponse<StreetDto> update(Long streetId, StreetDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.update(streetId, dto, headers);
        return responseHandler.processResponse(response, StreetDto.class);
    }

    /**
     * Deletes a Street.
     *
     * @param streetId ID of Street to delete
     * @param headers  HTTP headers for authentication
     * @return API response with operation status
     */
    @Override
    public ApiResponse<Void> delete(Long streetId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.delete(streetId, headers);
        return responseHandler.processResponse(response, Void.class);
    }

    /**
     * Retrieves a Street by ID.
     *
     * @param streetId ID of Street to retrieve
     * @param headers  HTTP headers for authentication
     * @return API response with Street data
     */
    @Override
    public ApiResponse<StreetDto> getById(Long streetId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getById(streetId, headers);
        return responseHandler.processResponse(response, StreetDto.class);
    }

    /**
     * Retrieves paginated list of Streets with optional filters.
     *
     * @param nameStartWith Filter by name prefix (optional)
     * @param lastSeenName  Pagination cursor by name (optional)
     * @param lastSeenId    Pagination cursor by ID (optional)
     * @param size          Number of items per page
     * @param headers       HTTP headers for authentication
     * @return API response with paginated Street results
     */
    @Override
    public ApiResponse<SimpleSlice<StreetDto>> getAll(String nameStartWith, String lastSeenName, Long lastSeenId, int size, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getAll(nameStartWith, lastSeenName, lastSeenId, size, headers);
        return responseHandler.processResponseSimpleSlice(response, StreetDto.class);
    }
}
