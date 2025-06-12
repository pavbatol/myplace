package ru.pavbatol.myplace.gateway.profile.geo.street.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.gateway.app.access.RequiredRoles;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.profile.geo.street.service.StreetService;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.street.StreetDto;

@Slf4j
@RestController
@RequestMapping("$${api.prefix}/${app.mp.profile.label}/user/geo/streets")
@RequiredArgsConstructor
@Tag(name = "[Profile/Geo]Street: Private", description = "API for working with Street")
public class PrivateStreetController {

    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";
    private final StreetService service;

    @RequiredRoles(roles = {USER, ADMIN})
    @GetMapping("/{streetId}")
    @Operation(summary = "getById", description = "get Street")
    public ResponseEntity<ApiResponse<StreetDto>> getById(@PathVariable(value = "streetId") Long streetId,
                                                          @RequestHeader HttpHeaders headers) {
        log.debug("GET getById() with streetId: {}", streetId);
        ApiResponse<StreetDto> apiResponse = service.getById(streetId, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @RequiredRoles(roles = {USER, ADMIN})
    @GetMapping
    @Operation(summary = "getAll", description = "get Streets")
    public ResponseEntity<ApiResponse<SimpleSlice<StreetDto>>> getAll(@RequestParam(value = "nameStartWith", required = false) String nameStartWith,
                                                                      @RequestParam(value = "lastSeenName", required = false) String lastSeenName,
                                                                      @RequestParam(value = "lastSeenId", required = false) Long lastSeenId,
                                                                      @RequestParam(value = "size", defaultValue = "10") int size,
                                                                      @RequestHeader HttpHeaders headers) {
        log.debug("GET getAll() with nameStartWith: {}, lastSeenName: {}, lastSeenId: {}, size: {}",
                nameStartWith, lastSeenName, lastSeenId, size);
        ApiResponse<SimpleSlice<StreetDto>> apiResponse = service.getAll(nameStartWith, lastSeenName, lastSeenId, size, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
