package ru.pavbatol.myplace.geo.region.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.geo.region.dto.RegionDto;
import ru.pavbatol.myplace.geo.region.service.RegionService;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/user/geo/regions")
@RequiredArgsConstructor
@Tag(name = "Private: Region", description = "API for working with Region")
public class PrivateRegionController {

    private final RegionService service;

    @GetMapping("/{regionId}")
    @Operation(summary = "getById", description = "get Region")
    public ResponseEntity<RegionDto> getById(@PathVariable(value = "regionId") Long regionId) {
        log.debug("GET getById() with regionId: {}", regionId);
        RegionDto body = service.getById(regionId);

        return ResponseEntity.ok(body);
    }

    @GetMapping
    @Operation(summary = "getAll", description = "get Regions")
    public ResponseEntity<Slice<RegionDto>> getAll(@RequestParam(value = "nameStartWith", required = false) String nameStartWith,
                                                   @RequestParam(value = "lastSeenName", required = false) String lastSeenName,
                                                   @RequestParam(value = "lastSeenCountryName", required = false) String lastSeenCountryName,
                                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        log.debug("GET getAll() with nameStartWith: {}, lastSeenName: {}, lastSeenCountryName: {}, size: {}",
                nameStartWith, lastSeenName, lastSeenCountryName, size);
        Slice<RegionDto> body = service.getAll(nameStartWith, lastSeenName, lastSeenCountryName, size);

        return ResponseEntity.ok(body);
    }
}
