package ru.pavbatol.myplace.geo.country.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.geo.country.dto.CountryDto;
import ru.pavbatol.myplace.geo.country.service.CountryService;

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
    public ResponseEntity<Slice<CountryDto>> getAll(@RequestParam(value = "nameStartWith", required = false) String nameStartWith,
                                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        log.debug("GET getAll() with nameStartWith: {}, page: {}, size: {}", nameStartWith, page, size);
        Slice<CountryDto> body = service.getAll(nameStartWith, page, size);

        return ResponseEntity.ok(body);
    }
}
