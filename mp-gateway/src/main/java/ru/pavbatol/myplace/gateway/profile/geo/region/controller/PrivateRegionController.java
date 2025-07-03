package ru.pavbatol.myplace.gateway.profile.geo.region.controller;

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
import ru.pavbatol.myplace.gateway.profile.geo.region.service.RegionService;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.region.RegionDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * REST controller for private Region operations (accessible to authenticated users).
 * Provides read-only endpoints for accessing Region data.
 */
@Slf4j
@Validated
@RestController
@RequestMapping("${api.prefix}/${app.mp.profile.label}/user/geo/regions")
@RequiredArgsConstructor
@Tag(name = "[Profile/Geo]Region: Private", description = "API for working with Region")
public class PrivateRegionController {

    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";
    private final RegionService service;

    /**
     * Retrieves a single Region by ID (User/Admin access).
     * @param regionId ID of the Region to retrieve
     * @param headers HTTP request headers
     * @return ResponseEntity containing the requested Region
     */
    @RequiredRoles(roles = {USER, ADMIN})
    @GetMapping("/{regionId}")
    @Operation(summary = "getById", description = "get Region")
    public ResponseEntity<ApiResponse<RegionDto>> getById(@PathVariable(value = "regionId") Long regionId,
                                                          @RequestHeader HttpHeaders headers) {
        log.debug("GET getById() with regionId: {}", regionId);
        ApiResponse<RegionDto> apiResponse = service.getById(regionId, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * Retrieves paginated list of Regions with filtering options (User/Admin access).
     * @param nameStartWith Filter by region name prefix (optional)
     * @param lastSeenName Pagination cursor for region name (optional)
     * @param lastSeenCountryName Pagination cursor for country name (optional)
     * @param size Number of items per page (1-100, default: 10)
     * @param headers HTTP request headers
     * @return ResponseEntity containing paginated Region results
     */
    @RequiredRoles(roles = {USER, ADMIN})
    @GetMapping
    @Operation(summary = "getAll", description = "get Regions")
    public ResponseEntity<ApiResponse<SimpleSlice<RegionDto>>> getAll(@RequestParam(value = "nameStartWith", required = false) String nameStartWith,
                                                                      @RequestParam(value = "lastSeenName", required = false) String lastSeenName,
                                                                      @RequestParam(value = "lastSeenCountryName", required = false) String lastSeenCountryName,
                                                                      @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(100) int size,
                                                                      @RequestHeader HttpHeaders headers) {
        log.debug("GET getAll() with nameStartWith: {}, lastSeenName: {}, lastSeenCountryName: {}, size: {}",
                nameStartWith, lastSeenName, lastSeenCountryName, size);
        ApiResponse<SimpleSlice<RegionDto>> apiResponse = service.getAll(nameStartWith, lastSeenName, lastSeenCountryName, size, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
