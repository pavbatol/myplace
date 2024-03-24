package ru.pavbatol.myplace.geo.district.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.app.Util.Marker;
import ru.pavbatol.myplace.geo.district.dto.DistrictDto;
import ru.pavbatol.myplace.geo.district.service.DistrictService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping("admin/geo/districts")
@RequiredArgsConstructor
@Tag(name = "Admin: District", description = "API for working with District")
public class AdminDistrictController {

    private final DistrictService service;

    @PostMapping
    @Validated({Marker.OnCreate.class})
    @Operation(summary = "create", description = "creating new District")
    public ResponseEntity<DistrictDto> create(@RequestBody @Valid DistrictDto dto) {
        log.debug("POST create() with dto: {}", dto);
        DistrictDto body = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping("/{districtId}")
    @Operation(summary = "update", description = "updating District")
    public ResponseEntity<DistrictDto> update(@PathVariable(value = "districtId") Long districtId,
                                              @RequestBody @Valid DistrictDto dto) {
        log.debug("PATCH update() with districtId: {}, dto: {}", districtId, dto);
        DistrictDto body = service.update(districtId, dto);

        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{districtId}")
    @Operation(summary = "delete", description = "deleting District")
    public ResponseEntity<Void> delete(@PathVariable(value = "districtId") Long districtId) {
        log.debug("DELETE delete() with districtId: {}", districtId);
        service.delete(districtId);

        return ResponseEntity.noContent().build();
    }
}
