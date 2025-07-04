package ru.pavbatol.myplace.gateway.profile.geo.city.controller;

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
import ru.pavbatol.myplace.gateway.profile.geo.city.service.CityService;
import ru.pavbatol.myplace.shared.dto.profile.geo.city.CityDto;
import ru.pavbatol.myplace.shared.util.Marker;

import javax.validation.Valid;

/**
 * REST controller for administrative City operations.
 * Provides CRUD endpoints for managing City entities (Admin access only).
 */
@Slf4j
@Validated
@RestController
@RequestMapping("${api.prefix}/${app.mp.profile.label}/admin/geo/cities")
@RequiredArgsConstructor
@Tag(name = "[Profile/Geo]City: Admin", description = "API for working with City")
public class AdminCityController {

    private static final String ADMIN = "ADMIN";
    private final CityService service;

    /**
     * Creates a new City (Admin only).
     *
     * @param dto     City data including required fields
     * @param headers HTTP headers with authorization
     * @return API response with created City data
     */
    @RequiredRoles(roles = {ADMIN})
    @PostMapping
    @Validated({Marker.OnCreate.class})
    @Operation(summary = "create", description = "creating new City")
    public ResponseEntity<ApiResponse<CityDto>> create(@RequestBody @Valid CityDto dto,
                                                       @RequestHeader HttpHeaders headers) {
        log.debug("POST create() with dto: {}", dto);
        ApiResponse<CityDto> apiResponse = service.create(dto, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * Updates an existing City (Admin only).
     *
     * @param cityId  ID of the City to update
     * @param dto     Updated City data
     * @param headers HTTP headers with authorization
     * @return API response with updated City data
     */
    @RequiredRoles(roles = {ADMIN})
    @PatchMapping("/{cityId}")
    @Operation(summary = "update", description = "updating City")
    public ResponseEntity<ApiResponse<CityDto>> update(@PathVariable(value = "cityId") Long cityId,
                                                       @RequestBody @Valid CityDto dto,
                                                       @RequestHeader HttpHeaders headers) {
        log.debug("PATCH update() with cityId: {}, dto: {}", cityId, dto);
        ApiResponse<CityDto> apiResponse = service.update(cityId, dto, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * Deletes a City (Admin only).
     *
     * @param cityId  ID of the City to delete
     * @param headers HTTP headers with authorization
     * @return API response with operation status
     */
    @RequiredRoles(roles = {ADMIN})
    @DeleteMapping("/{cityId}")
    @Operation(summary = "delete", description = "deleting City")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable(value = "cityId") Long cityId,
                                                    @RequestHeader HttpHeaders headers) {
        log.debug("DELETE delete() with cityId: {}", cityId);
        ApiResponse<Void> apiResponse = service.delete(cityId, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
