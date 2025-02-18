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
    public ResponseEntity<Slice<HouseDto>> getAll(@RequestParam(value = "numberStartWith", required = false) String numberStartWith,
                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        log.debug("GET getAll() with numberStartWith: {}, page: {}, size: {}", numberStartWith, page, size);
        Slice<HouseDto> body = service.getAll(numberStartWith, page, size);

        return ResponseEntity.ok(body);
    }
}
