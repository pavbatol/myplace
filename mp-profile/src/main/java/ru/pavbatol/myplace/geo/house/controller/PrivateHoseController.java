package ru.pavbatol.myplace.geo.house.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.geo.house.dto.HouseDto;
import ru.pavbatol.myplace.geo.house.service.HouseService;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/user/geo/houses")
@RequiredArgsConstructor
@Tag(name = "Private: House", description = "API for working with House")
public class PrivateHoseController {

    private final HouseService service;

    @GetMapping("/{houseId}")
    @Operation(summary = "getById", description = "get House")
    public ResponseEntity<HouseDto> getById(@PathVariable(value = "houseId") Long houseId) {
        log.debug("GET getById() with houseId: {}", houseId);
        HouseDto body = service.getById(houseId);

        return ResponseEntity.ok(body);
    }

    @GetMapping
    @Operation(summary = "getAll", description = "get Houses")
    public ResponseEntity<SimpleSlice<HouseDto>> getAll(@RequestParam(value = "numberStartWith", required = false) String numberStartWith,
                                                        @RequestParam(value = "lastSeenNumber", required = false) String lastSeenNumber,
                                                        @RequestParam(value = "lastSeenId", required = false) Long lastSeenId,
                                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        log.debug("GET getAll() with numberStartWith: {}, lastSeenNumber: {}, lastSeenId: {}, size: {}",
                numberStartWith, lastSeenNumber, lastSeenId, size);
        SimpleSlice<HouseDto> body = service.getAll(numberStartWith, lastSeenNumber, lastSeenId, size);

        return ResponseEntity.ok(body);
    }
}
