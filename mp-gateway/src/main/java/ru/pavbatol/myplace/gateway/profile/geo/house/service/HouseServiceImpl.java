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

/**
 * Service implementation for House operations.
 * Handles business logic and communicates with House client.
 */
@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {
    private final HouseClient client;
    private final ResponseHandler responseHandler;

    /**
     * Creates a new House record.
     *
     * @param dto     House data to create
     * @param headers HTTP headers for the request
     * @return API response with created House
     */
    @Override
    public ApiResponse<HouseDto> create(HouseDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.create(dto, headers);
        return responseHandler.processResponse(response, HouseDto.class);
    }

    /**
     * Updates an existing House record.
     *
     * @param houseId ID of House to update
     * @param dto     Updated House data
     * @param headers HTTP headers for the request
     * @return API response with updated House
     */
    @Override
    public ApiResponse<HouseDto> update(Long houseId, HouseDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.update(houseId, dto, headers);
        return responseHandler.processResponse(response, HouseDto.class);
    }

    /**
     * Deletes a House record.
     *
     * @param houseId ID of House to delete
     * @param headers HTTP headers for the request
     * @return API response with operation status
     */
    @Override
    public ApiResponse<Void> delete(Long houseId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.delete(houseId, headers);
        return responseHandler.processResponse(response, Void.class);
    }

    /**
     * Retrieves a House by ID.
     *
     * @param houseId ID of House to retrieve
     * @param headers HTTP headers for the request
     * @return API response with House data
     */
    @Override
    public ApiResponse<HouseDto> getById(Long houseId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getById(houseId, headers);
        return responseHandler.processResponse(response, HouseDto.class);
    }

    /**
     * Retrieves paginated list of Houses with number filtering.
     *
     * @param numberStartWith Filter by house number prefix (optional)
     * @param lastSeenNumber  Pagination cursor by house number (optional)
     * @param lastSeenId      Pagination cursor by ID (optional)
     * @param size            Number of items per page
     * @param headers         HTTP headers for the request
     * @return API response with paginated House results
     */
    @Override
    public ApiResponse<SimpleSlice<HouseDto>> getAll(String numberStartWith, String lastSeenNumber, Long lastSeenId, int size, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getAll(numberStartWith, lastSeenNumber, lastSeenId, size, headers);
        return responseHandler.processResponseSimpleSlice(response, HouseDto.class);
    }
}
