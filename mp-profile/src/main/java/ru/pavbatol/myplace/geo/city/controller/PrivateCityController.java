package ru.pavbatol.myplace.geo.city.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.geo.city.service.CityService;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.city.CityDto;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/user/geo/cities")
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
    public ResponseEntity<SimpleSlice<CityDto>> getAll(@RequestParam(value = "nameStartWith", required = false) String nameStartWith,
                                                       @RequestParam(value = "lastSeenName", required = false) String lastSeenName,
                                                       @RequestParam(value = "lastSeenId", required = false) Long lastSeenId,
                                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        log.debug("GET getAll() with nameStartWith: {}, lastSeenName: {}, lastSeenId: {}, size: {}",
                nameStartWith, lastSeenName, lastSeenId, size);
        SimpleSlice<CityDto> body = service.getAll(nameStartWith, lastSeenName, lastSeenId, size);

        return ResponseEntity.ok(body);
    }
}
