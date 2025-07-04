package ru.pavbatol.myplace.geo.region.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.geo.region.service.RegionService;
import ru.pavbatol.myplace.shared.dto.profile.geo.region.RegionDto;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/geo/regions")
@RequiredArgsConstructor
@Tag(name = "Admin: Region", description = "API for working with Region")
public class AdminRegionController {

    private final RegionService service;

    @PostMapping
    @Operation(summary = "create", description = "creating new Region")
    public ResponseEntity<RegionDto> create(@RequestBody RegionDto dto) {
        log.debug("POST create() with dto: {}", dto);
        RegionDto body = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping("/{regionId}")
    @Operation(summary = "update", description = "updating Region")
    public ResponseEntity<RegionDto> update(@PathVariable(value = "regionId") Long regionId,
                                            @RequestBody RegionDto dto) {
        log.debug("PATCH update() with regionId: {}, dto: {}", regionId, dto);
        RegionDto body = service.update(regionId, dto);

        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{regionId}")
    @Operation(summary = "delete", description = "deleting Region")
    public ResponseEntity<Void> delete(@PathVariable(value = "regionId") Long regionId) {
        log.debug("DELETE delete() with regionId: {}", regionId);
        service.delete(regionId);

        return ResponseEntity.noContent().build();
    }
}
