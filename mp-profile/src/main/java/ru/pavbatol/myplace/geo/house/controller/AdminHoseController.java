package ru.pavbatol.myplace.geo.house.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.app.util.Marker;
import ru.pavbatol.myplace.geo.house.service.HouseService;
import ru.pavbatol.myplace.shared.dto.profile.geo.house.HouseDto;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping("${api.prefix}/admin/geo/houses")
@RequiredArgsConstructor
@Tag(name = "Admin: House", description = "API for working with House")
public class AdminHoseController {

    private final HouseService service;

    @PostMapping
    @Validated({Marker.OnCreate.class})
    @Operation(summary = "create", description = "creating new House")
    public ResponseEntity<HouseDto> create(@RequestBody @Valid HouseDto dto) {
        log.debug("POST create() with dto: {}", dto);
        HouseDto body = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping("/{houseId}")
    @Operation(summary = "update", description = "updating House")
    public ResponseEntity<HouseDto> update(@PathVariable(value = "houseId") Long houseId,
                                           @RequestBody @Valid HouseDto dto) {
        log.debug("PATCH update() with houseId: {}, dto: {}", houseId, dto);
        HouseDto body = service.update(houseId, dto);

        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{houseId}")
    @Operation(summary = "delete", description = "deleting House")
    public ResponseEntity<Void> delete(@PathVariable(value = "houseId") Long houseId) {
        log.debug("DELETE delete() with houseId: {}", houseId);
        service.delete(houseId);

        return ResponseEntity.noContent().build();
    }
}
