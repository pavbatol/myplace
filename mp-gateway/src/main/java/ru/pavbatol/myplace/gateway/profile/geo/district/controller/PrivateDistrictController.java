package ru.pavbatol.myplace.gateway.profile.geo.district.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.gateway.app.access.RequiredRoles;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.profile.geo.district.service.DistrictService;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.district.DistrictDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * REST controller for private District operations (User/Admin access).
 * Provides read-only endpoints for accessing District data.
 */
@Slf4j
@RestController
@RequestMapping("${api.prefix}/${app.mp.profile.label}/user/geo/districts")
@RequiredArgsConstructor
@Tag(name = "[Profile/Geo]District: Private", description = "API for working with District")
public class PrivateDistrictController {

    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";
    private final DistrictService service;

    /**
     * Retrieves a District by ID (User/Admin access).
     *
     * @param districtId ID of the District to retrieve
     * @param headers    HTTP request headers
     * @return ResponseEntity containing the requested District
     */
    @RequiredRoles(roles = {USER, ADMIN})
    @GetMapping("/{districtId}")
    @Operation(summary = "getById", description = "get District")
    public ResponseEntity<ApiResponse<DistrictDto>> getById(@PathVariable(value = "districtId") Long districtId,
                                                            @RequestHeader HttpHeaders headers) {
        log.debug("GET getById() with districtId: {}", districtId);
        ApiResponse<DistrictDto> apiResponse = service.getById(districtId, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * Retrieves paginated list of Districts with filtering (User/Admin access).
     *
     * @param nameStartWith Filter by name prefix (optional)
     * @param lastSeenName  Pagination cursor for name (optional)
     * @param lastSeenId    Pagination cursor for ID (optional)
     * @param size          Number of items per page (1-100, default: 10)
     * @param headers       HTTP request headers
     * @return ResponseEntity containing paginated District results
     */
    @RequiredRoles(roles = {USER, ADMIN})
    @GetMapping
    @Operation(summary = "getAll", description = "get Districts")
    public ResponseEntity<ApiResponse<SimpleSlice<DistrictDto>>> getAll(@RequestParam(value = "nameStartWith", required = false) String nameStartWith,
                                                                        @RequestParam(value = "lastSeenName", required = false) String lastSeenName,
                                                                        @RequestParam(value = "lastSeenId", required = false) Long lastSeenId,
                                                                        @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(100) int size,
                                                                        @RequestHeader HttpHeaders headers) {
        log.debug("GET getAll() with nameStartWith: {}, lastSeenName: {}, lastSeenId: {}, size: {}",
                nameStartWith, lastSeenName, lastSeenId, size);
        ApiResponse<SimpleSlice<DistrictDto>> apiResponse = service.getAll(nameStartWith, lastSeenName, lastSeenId, size, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
