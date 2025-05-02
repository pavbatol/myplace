package ru.pavbatol.myplace.gateway.profile.geo.country.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.profile.geo.country.service.CountryService;
import ru.pavbatol.myplace.shared.dto.profile.geo.country.CountryDto;

import javax.validation.constraints.PositiveOrZero;


@Slf4j
@Validated
@RestController
@RequestMapping("${api.prefix}/user/geo/countries")
@RequiredArgsConstructor
@Tag(name = "Private: Country", description = "API for working with Country")
public class PrivateCountryController {

    private final CountryService service;

    @GetMapping("/{countryId}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "getById", description = "get Country")
    public ResponseEntity<ApiResponse<CountryDto>> getById(@PositiveOrZero @PathVariable(value = "countryId") Long countryId,
                                                           @RequestHeader HttpHeaders headers) {
        log.debug("GET getById() with countryId: {}", countryId);
        ApiResponse<CountryDto> apiResponse = service.getById(countryId, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @GetMapping
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "getAll", description = "get Countries")
    public ResponseEntity<ApiResponse<Slice<CountryDto>>> getAll(@RequestParam(value = "nameStartWith", required = false) String nameStartWith,
                                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                                                 @RequestHeader HttpHeaders headers) {
        log.debug("GET getAll() with nameStartWith: {}, page: {}, size: {}", nameStartWith, page, size);
        ApiResponse<Slice<CountryDto>> apiResponse = service.getAll(nameStartWith, page, size, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
