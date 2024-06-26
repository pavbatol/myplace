package ru.pavbatol.myplace.server.shipping.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoAddRequest;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoAddResponse;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoResponse;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoSearchFilter;
import ru.pavbatol.myplace.server.shipping.model.ShippingGeo;
import ru.pavbatol.myplace.server.shipping.mupper.ShippingGeoMapper;
import ru.pavbatol.myplace.server.shipping.repository.ShippingGeoMongoRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShippingGeoServiceImpl implements ShippingGeoService {
    private final ShippingGeoMongoRepository repository;
    private final ShippingGeoMapper mapper;

    @Override
    public Mono<ShippingGeoDtoAddResponse> add(ShippingGeoDtoAddRequest dto) {
        ShippingGeo shippingGeo = mapper.toEntity(dto);
        if (shippingGeo.getTimestamp() == null) {
            shippingGeo.setTimestamp(LocalDateTime.now());
        }
        return repository.save(shippingGeo).map(mapper::toDtoAddResponse);
    }

    @Override
    public Flux<ShippingGeoDtoResponse> findShippingCountryCities(ShippingGeoSearchFilter filter) {
        return repository.findShippingCountryCities(filter.setNullFieldsToDefault());
    }
}
