package ru.pavbatol.myplace.geo.country.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.geo.country.service.CountryService;
import ru.pavbatol.myplace.shared.dto.profile.geo.country.CountryDto;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/geo/countries")
@RequiredArgsConstructor
@Tag(name = "Admin: Country", description = "API for working with Country")
public class AdminCountryController {

    private final CountryService service;

    @PostMapping
    @Operation(summary = "create", description = "creating new Country")
    public ResponseEntity<CountryDto> create(@RequestBody CountryDto dto) {
        log.debug("POST create() with dto: {}", dto);
        CountryDto body = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping("/{countryId}")
    @Operation(summary = "update", description = "updating Country")
    public ResponseEntity<CountryDto> update(@PathVariable(value = "countryId") Long countryId,
                                             @RequestBody CountryDto dto) {
        log.debug("PATCH update() with countryId: {}, dto: {}", countryId, dto);
        CountryDto body = service.update(countryId, dto);

        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{countryId}")
    @Operation(summary = "delete", description = "deleting Country")
    public ResponseEntity<Void> delete(@PathVariable(value = "countryId") Long countryId) {
        log.debug("DELETE delete() with countryId: {}", countryId);
        service.delete(countryId);

        return ResponseEntity.noContent().build();
    }
}
