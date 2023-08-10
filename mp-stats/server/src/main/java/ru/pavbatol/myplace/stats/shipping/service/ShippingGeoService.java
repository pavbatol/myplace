package ru.pavbatol.myplace.stats.shipping.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoAddRequest;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoAddResponse;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoResponse;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoSearchFilter;

public interface ShippingGeoService {
    Mono<ShippingGeoDtoAddResponse> add(ShippingGeoDtoAddRequest dto);

    Flux<ShippingGeoDtoResponse> findShippingCountryCities(ShippingGeoSearchFilter filter);
}
