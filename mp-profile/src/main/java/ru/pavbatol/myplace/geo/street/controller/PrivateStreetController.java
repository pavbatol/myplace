package ru.pavbatol.myplace.geo.street.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.geo.street.service.StreetService;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.street.StreetDto;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/user/geo/streets")
@RequiredArgsConstructor
@Tag(name = "Private: Street", description = "API for working with Street")
public class PrivateStreetController {

    private final StreetService service;

    @GetMapping("/{streetId}")
    @Operation(summary = "getById", description = "get Street")
    public ResponseEntity<StreetDto> getById(@PathVariable(value = "streetId") Long streetId) {
        log.debug("GET getById() with streetId: {}", streetId);
        StreetDto body = service.getById(streetId);

        return ResponseEntity.ok(body);
    }

    @GetMapping
    @Operation(summary = "getAll", description = "get Streets")
    public ResponseEntity<SimpleSlice<StreetDto>> getAll(@RequestParam(value = "nameStartWith", required = false) String nameStartWith,
                                                         @RequestParam(value = "lastSeenName", required = false) String lastSeenName,
                                                         @RequestParam(value = "lastSeenId", required = false) Long lastSeenId,
                                                         @RequestParam(value = "size", defaultValue = "10") int size) {
        log.debug("GET getAll() with nameStartWith: {}, lastSeenName: {}, lastSeenId: {}, size: {}",
                nameStartWith, lastSeenName, lastSeenId, size);
        SimpleSlice<StreetDto> body = service.getAll(nameStartWith, lastSeenName, lastSeenId, size);

        return ResponseEntity.ok(body);
    }
}
