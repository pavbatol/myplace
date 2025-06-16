package ru.pavbatol.myplace.gateway.profile.geo.house.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.gateway.app.access.RequiredRoles;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.profile.geo.house.service.HouseService;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.house.HouseDto;

@Slf4j
@RestController
@RequestMapping("$${api.prefix}/${app.mp.profile.label}/user/geo/houses")
@RequiredArgsConstructor
@Tag(name = "[Profile/Geo]House: Private", description = "API for working with House")
public class PrivateHoseController {

    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";
    private final HouseService service;

    @RequiredRoles(roles = {USER, ADMIN})
    @GetMapping("/{houseId}")
    @Operation(summary = "getById", description = "get House")
    public ResponseEntity<ApiResponse<HouseDto>> getById(@PathVariable(value = "houseId") Long houseId,
                                                         @RequestHeader HttpHeaders headers) {
        log.debug("GET getById() with houseId: {}", houseId);
        ApiResponse<HouseDto> apiResponse = service.getById(houseId, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @RequiredRoles(roles = {USER, ADMIN})
    @GetMapping
    @Operation(summary = "getAll", description = "get Houses")
    public ResponseEntity<ApiResponse<SimpleSlice<HouseDto>>> getAll(@RequestParam(value = "numberStartWith", required = false) String numberStartWith,
                                                                     @RequestParam(value = "lastSeenNumber", required = false) String lastSeenNumber,
                                                                     @RequestParam(value = "lastSeenId", required = false) Long lastSeenId,
                                                                     @RequestParam(value = "size", defaultValue = "10") int size,
                                                                     @RequestHeader HttpHeaders headers) {
        log.debug("GET getAll() with numberStartWith: {}, lastSeenNumber: {}, lastSeenId: {}, size: {}",
                numberStartWith, lastSeenNumber, lastSeenId, size);
        ApiResponse<SimpleSlice<HouseDto>> apiResponse = service.getAll(numberStartWith, lastSeenNumber, lastSeenId, size, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
