package ru.pavbatol.myplace.client.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.client.AbstractStatsClient;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoAddRequest;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoAddResponse;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoResponse;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoSearchFilter;

@Slf4j
public class ShippingGeoClient
        extends AbstractStatsClient<ShippingGeoDtoAddRequest, ShippingGeoDtoAddResponse, ShippingGeoSearchFilter, ShippingGeoDtoResponse> {

    public static final String SHIPPINGGEOS = "shippinggeos";

    public ShippingGeoClient(String serverUrl) {
        super(serverUrl);
    }

    @Override
    public Mono<ShippingGeoDtoAddResponse> add(@NonNull ShippingGeoDtoAddRequest dto) {
        String path = UriComponentsBuilder.newInstance()
                .pathSegment(STATS)
                .pathSegment(SHIPPINGGEOS)
                .build().toUriString();

        return post(path, dto, ShippingGeoDtoAddResponse.class);
    }

    @Override
    public Flux<ShippingGeoDtoResponse> find(@NonNull ShippingGeoSearchFilter filter) {
        String path = UriComponentsBuilder.newInstance()
                .pathSegment(STATS)
                .pathSegment(SHIPPINGGEOS)
                .query(filter.toQuery(formatter))
                .build().toUriString();

        return get(path, ShippingGeoDtoResponse.class);
    }
}
