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

/**
 * Service implementation for District operations.
 * Handles business logic and communicates with District client.
 */
@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {
    private final DistrictClient client;
    private final ResponseHandler responseHandler;

    /**
     * Creates a new District.
     * @param dto District data transfer object
     * @param headers HTTP request headers
     * @return ApiResponse with created District
     */
    @Override
    public ApiResponse<DistrictDto> create(DistrictDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.create(dto, headers);
        return responseHandler.processResponse(response, DistrictDto.class);
    }

    /**
     * Updates an existing District.
     * @param districtId ID of the District to update
     * @param dto Updated District data
     * @param headers HTTP request headers
     * @return ApiResponse with updated District
     */
    @Override
    public ApiResponse<DistrictDto> update(Long districtId, DistrictDto dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.update(districtId, dto, headers);
        return responseHandler.processResponse(response, DistrictDto.class);
    }

    /**
     * Deletes a District.
     * @param districtId ID of the District to delete
     * @param headers HTTP request headers
     * @return ApiResponse with operation status
     */
    @Override
    public ApiResponse<Void> delete(Long districtId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.delete(districtId, headers);
        return responseHandler.processResponse(response, Void.class);
    }

    /**
     * Retrieves a District by ID.
     * @param districtId ID of the District to retrieve
     * @param headers HTTP request headers
     * @return ApiResponse with requested District
     */
    @Override
    public ApiResponse<DistrictDto> getById(Long districtId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getById(districtId, headers);
        return responseHandler.processResponse(response, DistrictDto.class);
    }

    /**
     * Retrieves paginated list of Districts with filtering.
     * @param nameStartWith Filter by name prefix (optional)
     * @param lastSeenName Pagination cursor for name (optional)
     * @param lastSeenId Pagination cursor for ID (optional)
     * @param size Number of items per page (1-100)
     * @param headers HTTP request headers
     * @return ApiResponse with paginated District results
     */
    @Override
    public ApiResponse<SimpleSlice<DistrictDto>> getAll(String nameStartWith, String lastSeenName, Long lastSeenId, int size, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getAll(nameStartWith, lastSeenName, lastSeenId, size, headers);
        return responseHandler.processResponseSimpleSlice(response, DistrictDto.class);
    }
}
