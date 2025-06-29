package ru.pavbatol.myplace.geo.city.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.app.util.Marker;
import ru.pavbatol.myplace.geo.city.service.CityService;
import ru.pavbatol.myplace.shared.dto.profile.geo.city.CityDto;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping("${api.prefix}/admin/geo/cities")
@RequiredArgsConstructor
@Tag(name = "Admin: City", description = "API for working with City")
public class AdminCityController {

    private final CityService service;

    @PostMapping
    @Validated({Marker.OnCreate.class})
    @Operation(summary = "create", description = "creating new City")
    public ResponseEntity<CityDto> create(@RequestBody @Valid CityDto dto) {
        log.debug("POST create() with dto: {}", dto);
        CityDto body = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping("/{cityId}")
    @Operation(summary = "update", description = "updating City")
    public ResponseEntity<CityDto> update(@PathVariable(value = "cityId") Long cityId,
                                          @RequestBody @Valid CityDto dto) {
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
}
