package ru.pavbatol.myplace.gateway.profile.geo.country.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.access.RequiredRoles;
import ru.pavbatol.myplace.gateway.profile.geo.country.service.CountryService;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.country.CountryDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

/**
 * REST controller for private Country operations (accessible to authenticated users).
 * Provides read-only endpoints for accessing Country data.
 */
@Slf4j
@Validated
@RestController
@RequestMapping("${api.prefix}/${app.mp.profile.label}/user/geo/countries")
@RequiredArgsConstructor
@Tag(name = "[Profile/Geo]Country: Private", description = "API for working with Country")
public class PrivateCountryController {

    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";
    private final CountryService service;

    /**
     * Retrieves a single Country by ID.
     * @param countryId ID of the Country to retrieve
     * @param headers HTTP request headers
     * @return ResponseEntity containing the requested Country
     */
    @RequiredRoles(roles = {USER, ADMIN})
    @GetMapping("/{countryId}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "getById", description = "get Country")
    public ResponseEntity<ApiResponse<CountryDto>> getById(@PositiveOrZero @PathVariable(value = "countryId") Long countryId,
                                                           @RequestHeader HttpHeaders headers) {
        log.debug("GET getById() with countryId: {}", countryId);
        ApiResponse<CountryDto> apiResponse = service.getById(countryId, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * Retrieves a paginated list of Countries with optional filtering.
     * @param nameStartWith Filter countries by name prefix (optional)
     * @param lastSeenName Pagination cursor (last seen country name)
     * @param size Number of items per page (1-100, default: 10)
     * @param headers HTTP request headers
     * @return ResponseEntity containing paginated Country results
     */
    @RequiredRoles(roles = {USER, ADMIN})
    @GetMapping
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "getAll", description = "get Countries by filter and pagination cursor")
    public ResponseEntity<ApiResponse<SimpleSlice<CountryDto>>> getAll(@RequestParam(value = "nameStartWith", required = false) String nameStartWith,
                                                                       @RequestParam(value = "lastSeenName", required = false) String lastSeenName,
                                                                       @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(100) int size,
                                                                       @RequestHeader HttpHeaders headers) {
        log.debug("GET getAll() with nameStartWith: {}, lastSeenName: {}, size: {}", nameStartWith, lastSeenName, size);
        ApiResponse<SimpleSlice<CountryDto>> apiResponse = service.getAll(nameStartWith, lastSeenName, size, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
