package ru.pavbatol.myplace.stats.shipping.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoAddRequest;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoAddResponse;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoResponse;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoSearchFilter;
import ru.pavbatol.myplace.stats.shipping.service.ShippingGeoService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
@Tag(name = "ShippingGeos", description = "API for working with 'ShippingGeo' entity")
public class ShippingGeoController {

    private final ShippingGeoService service;

    @PostMapping("/shippinggeos")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add", description = "adding a ShippingGeo")
    public Mono<ShippingGeoDtoAddResponse> add(@Valid @RequestBody ShippingGeoDtoAddRequest dto) {
        log.debug("POST (add) with dto={},", dto);
        return service.add(dto);
    }

    @GetMapping("/shippinggeos")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "findShippingCountryCities", description = "finding ShippingGeo by filter")
    public Flux<ShippingGeoDtoResponse> findShippingCountryCities(@Valid ShippingGeoSearchFilter filter) {
        log.debug("GET (findShippingCountryCities) with filter={}", filter);
        return service.findShippingCountryCities(filter);
    }
}
