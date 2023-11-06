package ru.pavbatol.myplace.geo.city.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.geo.city.dto.CityDto;
import ru.pavbatol.myplace.geo.city.model.City;
import ru.pavbatol.myplace.geo.city.service.CityService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/geo/cities")
@RequiredArgsConstructor
@Tag(name = "Private: City", description = "API for working with City")
public class CityController {

    private final CityService service;

    @PostMapping
    @Operation(summary = "create", description = "creating new City")
    public ResponseEntity<CityDto> create(@Valid @RequestBody City dto) {
        log.debug("POST create() with dto: {}", dto);
        CityDto body = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping("/{cityId}")
    @Operation(summary = "update", description = "updating City")
    public ResponseEntity<CityDto> update(@PathVariable(value = "cityId") Long cityId,
                                          @Valid @RequestBody CityDto dto) {
        log.debug("PATCH update() with cityId: {}, dto: {}", cityId, dto);
        CityDto body = service.update(cityId, dto);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{cityId}")
    @Operation(summary = "delete", description = "deleting City")
    public ResponseEntity<Void> delete(@PathVariable(value = "cityId") Long cityId) {
        log.debug("DELETE delete() with cityId: {}", cityId);
        service.delete(cityId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cityId}")
    @Operation(summary = "getById", description = "get City")
    public ResponseEntity<CityDto> getById(@PathVariable(value = "cityId") Long cityId) {
        log.debug("GET getById() with cityId: {}", cityId);
        CityDto body = service.getById(cityId);
        return ResponseEntity.ok(body);
    }
}
