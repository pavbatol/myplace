package ru.pavbatol.myplace.geo.city.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.geo.city.dto.CityDto;
import ru.pavbatol.myplace.geo.city.service.CityService;

@Slf4j
@RestController
@RequestMapping("user/geo/cities")
@RequiredArgsConstructor
@Tag(name = "Private: City", description = "API for working with City")
public class PrivateCityController {

    private final CityService service;

    @GetMapping("/{cityId}")
    @Operation(summary = "getById", description = "get City")
    public ResponseEntity<CityDto> getById(@PathVariable(value = "cityId") Long cityId) {
        log.debug("GET getById() with cityId: {}", cityId);
        CityDto body = service.getById(cityId);

        return ResponseEntity.ok(body);
    }

    @GetMapping
    @Operation(summary = "getAll", description = "get Cities")
    public ResponseEntity<Slice<CityDto>> getAll(@RequestParam(value = "nameStartWith", required = false) String nameStartWith,
                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        log.debug("GET getAll() with nameStartWith: {}, page: {}, size: {}", nameStartWith, page, size);
        Slice<CityDto> body = service.getAll(nameStartWith, page, size);

        return ResponseEntity.ok(body);
    }
}
