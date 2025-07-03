package ru.pavbatol.myplace.gateway.profile.geo.district.controller;

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
import ru.pavbatol.myplace.gateway.profile.geo.district.service.DistrictService;
import ru.pavbatol.myplace.shared.dto.profile.geo.district.DistrictDto;
import ru.pavbatol.myplace.shared.util.Marker;

import javax.validation.Valid;

/**
 * REST controller for administrative District operations.
 * Provides CRUD endpoints for managing District entities (Admin access only).
 */
@Slf4j
@Validated
@RestController
@RequestMapping("${api.prefix}/${app.mp.profile.label}/admin/geo/districts")
@RequiredArgsConstructor
@Tag(name = "[Profile/Geo]District: Admin", description = "API for working with District")
public class AdminDistrictController {

    private static final String ADMIN = "ADMIN";
    private final DistrictService service;

    /**
     * Creates a new District (Admin only).
     * @param dto District data transfer object
     * @param headers HTTP request headers
     * @return ResponseEntity with created District
     */
    @RequiredRoles(roles = {ADMIN})
    @PostMapping
    @Validated({Marker.OnCreate.class})
    @Operation(summary = "create", description = "creating new District")
    public ResponseEntity<ApiResponse<DistrictDto>> create(@RequestBody @Valid DistrictDto dto,
                                                           @RequestHeader HttpHeaders headers) {
        log.debug("POST create() with dto: {}", dto);
        ApiResponse<DistrictDto> apiResponse = service.create(dto, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * Updates an existing District (Admin only).
     * @param districtId ID of the District to update
     * @param dto Updated District data
     * @param headers HTTP request headers
     * @return ResponseEntity with updated District
     */
    @RequiredRoles(roles = {ADMIN})
    @PatchMapping("/{districtId}")
    @Operation(summary = "update", description = "updating District")
    public ResponseEntity<ApiResponse<DistrictDto>> update(@PathVariable(value = "districtId") Long districtId,
                                                           @RequestBody @Valid DistrictDto dto,
                                                           @RequestHeader HttpHeaders headers) {
        log.debug("PATCH update() with districtId: {}, dto: {}", districtId, dto);
        ApiResponse<DistrictDto> apiResponse = service.update(districtId, dto, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * Deletes a District (Admin only).
     * @param districtId ID of the District to delete
     * @param headers HTTP request headers
     * @return ResponseEntity with operation status
     */
    @RequiredRoles(roles = {ADMIN})
    @DeleteMapping("/{districtId}")
    @Operation(summary = "delete", description = "deleting District")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable(value = "districtId") Long districtId,
                                                    @RequestHeader HttpHeaders headers) {
        log.debug("DELETE delete() with districtId: {}", districtId);
        ApiResponse<Void> apiResponse = service.delete(districtId, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
