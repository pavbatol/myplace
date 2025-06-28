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
import ru.pavbatol.myplace.shared.dto.profile.geo.street.StreetDto;
import ru.pavbatol.myplace.shared.util.Marker;

import javax.validation.Valid;

/**
 * Administrative REST controller for Street operations.
 * Provides CRUD endpoints for Street management (Admin access only).
 */
@Slf4j
@Validated
@RestController
@RequestMapping("${api.prefix}/${app.mp.profile.label}/admin/geo/streets")
@RequiredArgsConstructor
@Tag(name = "[Profile/Geo]Street: Admin", description = "API for working with Street")
public class AdminStreetController {

    private static final String ADMIN = "ADMIN";
    private final StreetService service;

    /**
     * Creates a new Street (Admin only).
     *
     * @param dto     Street data transfer object
     * @param headers HTTP headers with authorization
     * @return API response with created Street
     */
    @RequiredRoles(roles = {ADMIN})
    @PostMapping
    @Validated({Marker.OnCreate.class})
    @Operation(summary = "create", description = "creating new Street")
    public ResponseEntity<ApiResponse<StreetDto>> create(@RequestBody @Valid StreetDto dto,
                                                         @RequestHeader HttpHeaders headers) {
        log.debug("POST create() with dto: {}", dto);
        ApiResponse<StreetDto> apiResponse = service.create(dto, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * Updates an existing Street (Admin only).
     *
     * @param streetId ID of Street to update
     * @param dto      Updated Street data
     * @param headers  HTTP headers with authorization
     * @return API response with updated Street
     */
    @RequiredRoles(roles = {ADMIN})
    @PatchMapping("/{streetId}")
    @Operation(summary = "update", description = "updating Street")
    public ResponseEntity<ApiResponse<StreetDto>> update(@PathVariable(value = "streetId") Long streetId,
                                                         @RequestBody @Valid StreetDto dto,
                                                         @RequestHeader HttpHeaders headers) {
        log.debug("PATCH update() with streetId: {}, dto: {}", streetId, dto);
        ApiResponse<StreetDto> apiResponse = service.update(streetId, dto, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * Deletes a Street (Admin only).
     *
     * @param streetId ID of Street to delete
     * @param headers  HTTP headers with authorization
     * @return API response with operation status
     */
    @RequiredRoles(roles = {ADMIN})
    @DeleteMapping("/{streetId}")
    @Operation(summary = "delete", description = "deleting Street")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable(value = "streetId") Long streetId,
                                                    @RequestHeader HttpHeaders headers) {
        log.debug("DELETE delete() with streetId: {}", streetId);
        ApiResponse<Void> apiResponse = service.delete(streetId, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
