package ru.pavbatol.myplace.gateway.profile.geo.street.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.gateway.app.access.RequiredRoles;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.profile.geo.street.service.StreetService;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.street.StreetDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * REST controller for Street read operations (User/Admin access).
 * Provides endpoints for retrieving Street information.
 */
@Slf4j
@Validated
@RestController
@RequestMapping("${api.prefix}/${app.mp.profile.label}/user/geo/streets")
@RequiredArgsConstructor
@Tag(name = "[Profile/Geo]Street: Private", description = "API for working with Street")
public class PrivateStreetController {

    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";
    private final StreetService service;

    /**
     * Retrieves a Street by ID (User/Admin access).
     *
     * @param streetId ID of the Street to retrieve
     * @param headers  HTTP headers with authorization
     * @return API response with Street data
     */
    @RequiredRoles(roles = {USER, ADMIN})
    @GetMapping("/{streetId}")
    @Operation(summary = "getById", description = "get Street")
    public ResponseEntity<ApiResponse<StreetDto>> getById(@PathVariable(value = "streetId") Long streetId,
                                                          @RequestHeader HttpHeaders headers) {
        log.debug("GET getById() with streetId: {}", streetId);
        ApiResponse<StreetDto> apiResponse = service.getById(streetId, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * Retrieves paginated list of Streets with optional filtering (User/Admin access).
     *
     * @param nameStartWith Filter by name prefix (optional)
     * @param lastSeenName  Pagination cursor by name (optional)
     * @param lastSeenId    Pagination cursor by ID (optional)
     * @param size          Number of items per page (default: 10)
     * @param headers       HTTP headers with authorization
     * @return API response with paginated Street results
     */
    @RequiredRoles(roles = {USER, ADMIN})
    @GetMapping
    @Operation(summary = "getAll", description = "get Streets")
    public ResponseEntity<ApiResponse<SimpleSlice<StreetDto>>> getAll(@RequestParam(value = "nameStartWith", required = false) String nameStartWith,
                                                                      @RequestParam(value = "lastSeenName", required = false) String lastSeenName,
                                                                      @RequestParam(value = "lastSeenId", required = false) Long lastSeenId,
                                                                      @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(100) int size,
                                                                      @RequestHeader HttpHeaders headers) {
        log.debug("GET getAll() with nameStartWith: {}, lastSeenName: {}, lastSeenId: {}, size: {}",
                nameStartWith, lastSeenName, lastSeenId, size);
        ApiResponse<SimpleSlice<StreetDto>> apiResponse = service.getAll(nameStartWith, lastSeenName, lastSeenId, size, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
