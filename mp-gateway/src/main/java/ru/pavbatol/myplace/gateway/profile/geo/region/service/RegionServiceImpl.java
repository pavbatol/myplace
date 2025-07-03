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

/**
 * Service implementation for Region operations.
 * Handles business logic and communicates with the Region client.
 */
@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {
    private final RegionClient client;
    private final ResponseHandler responseHandler;

    /**
     * Creates a new Region.
     * @param dto Region data transfer object
     * @param headers HTTP request headers
     * @return ApiResponse with created Region
     */
    @Override
    public ApiResponse<RegionDto> create(RegionDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.create(dto, headers);
        return responseHandler.processResponse(response, RegionDto.class);
    }

    /**
     * Updates an existing Region.
     * @param regionId ID of the Region to update
     * @param dto Updated Region data
     * @param headers HTTP request headers
     * @return ApiResponse with updated Region
     */
    @Override
    public ApiResponse<RegionDto> update(Long regionId, RegionDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.update(regionId, dto, headers);
        return responseHandler.processResponse(response, RegionDto.class);
    }

    /**
     * Deletes a Region.
     * @param regionId ID of the Region to delete
     * @param headers HTTP request headers
     * @return ApiResponse with operation status
     */
    @Override
    public ApiResponse<Void> delete(Long regionId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.delete(regionId, headers);
        return responseHandler.processResponse(response, Void.class);
    }

    /**
     * Retrieves a Region by ID.
     * @param regionId ID of the Region to retrieve
     * @param headers HTTP request headers
     * @return ApiResponse with requested Region
     */
    @Override
    public ApiResponse<RegionDto> getById(Long regionId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getById(regionId, headers);
        return responseHandler.processResponse(response, RegionDto.class);
    }

    /**
     * Retrieves paginated list of Regions with filtering.
     * @param nameStartWith Filter by name prefix (optional)
     * @param lastSeenName Pagination cursor for region name (optional)
     * @param lastSeenCountryName Pagination cursor for country name (optional)
     * @param size Number of items per page
     * @param headers HTTP request headers
     * @return ApiResponse with paginated Region results
     */
    @Override
    public ApiResponse<SimpleSlice<RegionDto>> getAll(String nameStartWith, String lastSeenName, String lastSeenCountryName, int size, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getAll(nameStartWith, lastSeenName, lastSeenCountryName, size, headers);
        return responseHandler.processResponseSimpleSlice(response, RegionDto.class);
    }
}
