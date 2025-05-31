package ru.pavbatol.myplace.geo.district.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.geo.district.dto.DistrictDto;
import ru.pavbatol.myplace.geo.district.service.DistrictService;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/user/geo/districts")
@RequiredArgsConstructor
@Tag(name = "Private: District", description = "API for working with District")
public class PrivateDistrictController {

    private final DistrictService service;

    @GetMapping("/{districtId}")
    @Operation(summary = "getById", description = "get District")
    public ResponseEntity<DistrictDto> getById(@PathVariable(value = "districtId") Long districtId) {
        log.debug("GET getById() with districtId: {}", districtId);
        DistrictDto body = service.getById(districtId);

        return ResponseEntity.ok(body);
    }

    @GetMapping
    @Operation(summary = "getAll", description = "get Districts")
    public ResponseEntity<SimpleSlice<DistrictDto>> getAll(@RequestParam(value = "nameStartWith", required = false) String nameStartWith,
                                                           @RequestParam(value = "lastSeenName", required = false) String lastSeenName,
                                                           @RequestParam(value = "lastSeenId", required = false) Long lastSeenId,
                                                           @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(100) int size) {
        log.debug("GET getAll() with nameStartWith: {}, lastSeenName: {}, lastSeenId: {}, size: {}",
                nameStartWith, lastSeenName, lastSeenId, size);
        SimpleSlice<DistrictDto> body = service.getAll(nameStartWith, lastSeenName, lastSeenId, size);

        return ResponseEntity.ok(body);
    }
}
