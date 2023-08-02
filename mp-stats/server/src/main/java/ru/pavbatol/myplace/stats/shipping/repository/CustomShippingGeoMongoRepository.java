package ru.pavbatol.myplace.stats.shipping.repository;

import reactor.core.publisher.Flux;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoResponse;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoSearchFilter;

public interface CustomShippingGeoMongoRepository {
    Flux<ShippingGeoDtoResponse> findShippingCountryCities(ShippingGeoSearchFilter filter);
}
