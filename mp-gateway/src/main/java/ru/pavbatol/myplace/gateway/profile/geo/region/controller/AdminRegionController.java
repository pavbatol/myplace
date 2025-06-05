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
import ru.pavbatol.myplace.shared.dto.profile.geo.region.RegionDto;
import ru.pavbatol.myplace.shared.util.Marker;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping("${api.prefix}/${app.mp.profile.label}/admin/geo/regions")
@RequiredArgsConstructor
@Tag(name = "[Profile/Geo]Region: Admin", description = "API for working with Region")
public class AdminRegionController {

    private static final String ADMIN = "ADMIN";
    private final RegionService service;

    @RequiredRoles(roles = {ADMIN})
    @PostMapping
    @Validated({Marker.OnCreate.class})
    @Operation(summary = "create", description = "creating new Region")
    public ResponseEntity<ApiResponse<RegionDto>> create(@RequestBody @Valid RegionDto dto,
                                                         @RequestHeader HttpHeaders headers) {
        log.debug("POST create() with dto: {}", dto);
        ApiResponse<RegionDto> apiResponse = service.create(dto, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @RequiredRoles(roles = {ADMIN})
    @PatchMapping("/{regionId}")
    @Operation(summary = "update", description = "updating Region")
    public ResponseEntity<ApiResponse<RegionDto>> update(@PathVariable(value = "regionId") Long regionId,
                                                         @RequestBody @Valid RegionDto dto,
                                                         @RequestHeader HttpHeaders headers) {
        log.debug("PATCH update() with regionId: {}, dto: {}", regionId, dto);
        ApiResponse<RegionDto> apiResponse = service.update(regionId, dto, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @RequiredRoles(roles = {ADMIN})
    @DeleteMapping("/{regionId}")
    @Operation(summary = "delete", description = "deleting Region")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable(value = "regionId") Long regionId,
                                                    @RequestHeader HttpHeaders headers) {
        log.debug("DELETE delete() with regionId: {}", regionId);
        ApiResponse<Void> apiResponse = service.delete(regionId, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
