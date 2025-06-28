package ru.pavbatol.myplace.gateway.profile.geo.city.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.gateway.app.access.RequiredRoles;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.profile.geo.city.service.CityService;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.city.CityDto;

/**
 * REST controller for City read operations (User/Admin access).
 * Provides endpoints for retrieving City information.
 */
@Slf4j
@RestController
@RequestMapping("$${api.prefix}/${app.mp.profile.label}/user/geo/cities")
@RequiredArgsConstructor
@Tag(name = "[Profile/Geo]City: Private", description = "API for working with City")
public class PrivateCityController {

    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";
    private final CityService service;

    /**
     * Retrieves a City by ID (User/Admin access).
     *
     * @param cityId  ID of the City to retrieve
     * @param headers HTTP headers with authorization
     * @return API response with City data
     */
    @RequiredRoles(roles = {USER, ADMIN})
    @GetMapping("/{cityId}")
    @Operation(summary = "getById", description = "get City")
    public ResponseEntity<ApiResponse<CityDto>> getById(@PathVariable(value = "cityId") Long cityId,
                                                        @RequestHeader HttpHeaders headers) {
        log.debug("GET getById() with cityId: {}", cityId);
        ApiResponse<CityDto> apiResponse = service.getById(cityId, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * Retrieves paginated list of Cities with optional filtering (User/Admin access).
     *
     * @param nameStartWith Filter cities by name prefix (optional)
     * @param lastSeenName  Pagination cursor by name (optional)
     * @param lastSeenId    Pagination cursor by ID (optional)
     * @param size          Number of items per page (default: 10)
     * @param headers       HTTP headers with authorization
     * @return API response with paginated City results
     */
    @RequiredRoles(roles = {USER, ADMIN})
    @GetMapping
    @Operation(summary = "getAll", description = "get Cities")
    public ResponseEntity<ApiResponse<SimpleSlice<CityDto>>> getAll(@RequestParam(value = "nameStartWith", required = false) String nameStartWith,
                                                                    @RequestParam(value = "lastSeenName", required = false) String lastSeenName,
                                                                    @RequestParam(value = "lastSeenId", required = false) Long lastSeenId,
                                                                    @RequestParam(value = "size", defaultValue = "10") int size,
                                                                    @RequestHeader HttpHeaders headers) {
        log.debug("GET getAll() with nameStartWith: {}, lastSeenName: {}, lastSeenId: {}, size: {}",
                nameStartWith, lastSeenName, lastSeenId, size);
        ApiResponse<SimpleSlice<CityDto>> apiResponse = service.getAll(nameStartWith, lastSeenName, lastSeenId, size, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
