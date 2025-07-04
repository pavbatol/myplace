package ru.pavbatol.myplace.geo.country.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.geo.country.service.CountryService;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.country.CountryDto;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/user/geo/countries")
@RequiredArgsConstructor
@Tag(name = "Private: Country", description = "API for working with Country")
public class PrivateCountryController {

    private final CountryService service;

    @GetMapping("/{countryId}")
    @Operation(summary = "getById", description = "get Country")
    public ResponseEntity<CountryDto> getById(@PathVariable(value = "countryId") Long countryId) {
        log.debug("GET getById() with countryId: {}", countryId);
        CountryDto body = service.getById(countryId);

        return ResponseEntity.ok(body);
    }

    @GetMapping
    @Operation(summary = "getAll", description = "get Countries")
    public ResponseEntity<SimpleSlice<CountryDto>> getAll(@RequestParam(value = "nameStartWith", required = false) String nameStartWith,
                                                          @RequestParam(value = "lastSeenName", required = false) String lastSeenName,
                                                          @RequestParam(value = "size", defaultValue = "10") int size) {
        log.debug("GET getAll() with nameStartWith: {}, lastSeenName: {}, size: {}", nameStartWith, lastSeenName, size);
        SimpleSlice<CountryDto> body = service.getAll(nameStartWith, lastSeenName, size);

        return ResponseEntity.ok(body);
    }
}
