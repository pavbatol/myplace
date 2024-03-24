package ru.pavbatol.myplace.geo.street.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.app.Util.Marker;
import ru.pavbatol.myplace.geo.street.dto.StreetDto;
import ru.pavbatol.myplace.geo.street.service.StreetService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping("admin/geo/streets")
@RequiredArgsConstructor
@Tag(name = "Admin: Street", description = "API for working with Street")
public class AdminStreetController {

    private final StreetService service;

    @PostMapping
    @Validated({Marker.OnCreate.class})
    @Operation(summary = "create", description = "creating new Street")
    public ResponseEntity<StreetDto> create(@RequestBody @Valid StreetDto dto) {
        log.debug("POST create() with dto: {}", dto);
        StreetDto body = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping("/{streetId}")
    @Operation(summary = "update", description = "updating Street")
    public ResponseEntity<StreetDto> update(@PathVariable(value = "streetId") Long streetId,
                                            @RequestBody @Valid StreetDto dto) {
        log.debug("PATCH update() with streetId: {}, dto: {}", streetId, dto);
        StreetDto body = service.update(streetId, dto);

        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{streetId}")
    @Operation(summary = "delete", description = "deleting Street")
    public ResponseEntity<Void> delete(@PathVariable(value = "streetId") Long streetId) {
        log.debug("DELETE delete() with streetId: {}", streetId);
        service.delete(streetId);

        return ResponseEntity.noContent().build();
    }
}
