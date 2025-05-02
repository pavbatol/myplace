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
import ru.pavbatol.myplace.gateway.profile.geo.country.service.CountryService;
import ru.pavbatol.myplace.shared.dto.profile.geo.country.CountryDto;
import ru.pavbatol.myplace.shared.util.Marker;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequestMapping("${api.prefix}/admin/geo/countries")
@RequiredArgsConstructor
@Tag(name = "Admin: Country", description = "API for working with Country")
public class AdminCountryController {

    private final CountryService service;

    @PostMapping
    @Validated({Marker.OnCreate.class})
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "create", description = "creating new Country")
    public ResponseEntity<ApiResponse<CountryDto>> create(@RequestBody @Valid CountryDto dto,
                                                          @RequestHeader HttpHeaders headers) {
        log.debug("POST create() with dto: {}", dto);
        ApiResponse<CountryDto> apiResponse = service.create(dto, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PatchMapping("/{countryId}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "update", description = "updating Country")
    public ResponseEntity<ApiResponse<CountryDto>> update(@PositiveOrZero @PathVariable(value = "countryId") Long countryId,
                                                          @RequestBody @Valid CountryDto dto,
                                                          @RequestHeader HttpHeaders headers) {
        log.debug("PATCH update() with countryId: {}, dto: {}", countryId, dto);
        ApiResponse<CountryDto> apiResponse = service.update(countryId, dto, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @DeleteMapping("/{countryId}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "delete", description = "deleting Country")
    public ResponseEntity<ApiResponse<Void>> delete(@PositiveOrZero @PathVariable(value = "countryId") Long countryId,
                                                    @RequestHeader HttpHeaders headers) {
        log.debug("DELETE delete() with countryId: {}", countryId);
        ApiResponse<Void> apiResponse = service.delete(countryId, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
