package ru.pavbatol.myplace.gateway.profile.geo.house.controller;

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
import ru.pavbatol.myplace.gateway.profile.geo.house.service.HouseService;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.house.HouseDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * REST controller for House read operations (User/Admin access).
 * Provides endpoints for retrieving House information.
 */
@Slf4j
@Validated
@RestController
@RequestMapping("${api.prefix}/${app.mp.profile.label}/user/geo/houses")
@RequiredArgsConstructor
@Tag(name = "[Profile/Geo]House: Private", description = "API for working with House")
public class PrivateHoseController {

    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";
    private final HouseService service;

    /**
     * Retrieves a House by ID (User/Admin access).
     *
     * @param houseId ID of the House to retrieve
     * @param headers HTTP headers with authorization
     * @return ResponseEntity containing the requested House
     */
    @RequiredRoles(roles = {USER, ADMIN})
    @GetMapping("/{houseId}")
    @Operation(summary = "getById", description = "get House")
    public ResponseEntity<ApiResponse<HouseDto>> getById(@PathVariable(value = "houseId") Long houseId,
                                                         @RequestHeader HttpHeaders headers) {
        log.debug("GET getById() with houseId: {}", houseId);
        ApiResponse<HouseDto> apiResponse = service.getById(houseId, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * Retrieves paginated list of Houses with optional filtering (User/Admin access).
     *
     * @param numberStartWith Filter by house number prefix (optional)
     * @param lastSeenNumber  Pagination cursor by house number (optional)
     * @param lastSeenId      Pagination cursor by ID (optional)
     * @param size            Number of items per page (default: 10)
     * @param headers         HTTP headers with authorization
     * @return ResponseEntity containing paginated House results
     */
    @RequiredRoles(roles = {USER, ADMIN})
    @GetMapping
    @Operation(summary = "getAll", description = "get Houses")
    public ResponseEntity<ApiResponse<SimpleSlice<HouseDto>>> getAll(@RequestParam(value = "numberStartWith", required = false) String numberStartWith,
                                                                     @RequestParam(value = "lastSeenNumber", required = false) String lastSeenNumber,
                                                                     @RequestParam(value = "lastSeenId", required = false) Long lastSeenId,
                                                                     @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(100) int size,
                                                                     @RequestHeader HttpHeaders headers) {
        log.debug("GET getAll() with numberStartWith: {}, lastSeenNumber: {}, lastSeenId: {}, size: {}",
                numberStartWith, lastSeenNumber, lastSeenId, size);
        ApiResponse<SimpleSlice<HouseDto>> apiResponse = service.getAll(numberStartWith, lastSeenNumber, lastSeenId, size, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
