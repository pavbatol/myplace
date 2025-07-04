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
import ru.pavbatol.myplace.shared.dto.profile.geo.house.HouseDto;
import ru.pavbatol.myplace.shared.util.Marker;

import javax.validation.Valid;

/**
 * REST controller for administrative House operations.
 * Provides CRUD endpoints for managing House entities (Admin access only).
 */
@Slf4j
@Validated
@RestController
@RequestMapping("${api.prefix}/${app.mp.profile.label}/admin/geo/houses")
@RequiredArgsConstructor
@Tag(name = "[Profile/Geo]House: Admin", description = "API for working with House")
public class AdminHoseController {

    private static final String ADMIN = "ADMIN";
    private final HouseService service;

    /**
     * Creates a new House (Admin only).
     *
     * @param dto     House data transfer object
     * @param headers HTTP headers with authorization
     * @return ResponseEntity with created House
     */
    @RequiredRoles(roles = {ADMIN})
    @PostMapping
    @Validated({Marker.OnCreate.class})
    @Operation(summary = "create", description = "creating new House")
    public ResponseEntity<ApiResponse<HouseDto>> create(@RequestBody @Valid HouseDto dto,
                                                        @RequestHeader HttpHeaders headers) {
        log.debug("POST create() with dto: {}", dto);
        ApiResponse<HouseDto> apiResponse = service.create(dto, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * Updates an existing House (Admin only).
     *
     * @param houseId ID of the House to update
     * @param dto     Updated House data
     * @param headers HTTP headers with authorization
     * @return ResponseEntity with updated House
     */
    @RequiredRoles(roles = {ADMIN})
    @PatchMapping("/{houseId}")
    @Operation(summary = "update", description = "updating House")
    public ResponseEntity<ApiResponse<HouseDto>> update(@PathVariable(value = "houseId") Long houseId,
                                                        @RequestBody @Valid HouseDto dto,
                                                        @RequestHeader HttpHeaders headers) {
        log.debug("PATCH update() with houseId: {}, dto: {}", houseId, dto);
        ApiResponse<HouseDto> apiResponse = service.update(houseId, dto, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * Deletes a House (Admin only).
     *
     * @param houseId ID of the House to delete
     * @param headers HTTP headers with authorization
     * @return ResponseEntity with operation status
     */
    @RequiredRoles(roles = {ADMIN})
    @DeleteMapping("/{houseId}")
    @Operation(summary = "delete", description = "deleting House")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable(value = "houseId") Long houseId,
                                                    @RequestHeader HttpHeaders headers) {
        log.debug("DELETE delete() with houseId: {}", houseId);
        ApiResponse<Void> apiResponse = service.delete(houseId, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
