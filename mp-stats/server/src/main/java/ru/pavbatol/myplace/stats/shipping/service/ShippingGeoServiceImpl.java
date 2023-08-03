package ru.pavbatol.myplace.stats.shipping.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.SortDirection;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoAddRequest;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoAddResponse;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoResponse;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoSearchFilter;
import ru.pavbatol.myplace.stats.shipping.model.ShippingGeo;
import ru.pavbatol.myplace.stats.shipping.mupper.ShippingGeoMapper;
import ru.pavbatol.myplace.stats.shipping.repository.ShippingGeoMongoRepository;

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
        filter.populateNullFields();
        return repository.findShippingCountryCities(filter.populateNullFields());

//        filter.populateNullFields();
//        return repository.findShippingCountryCitiesByDateAndUnique(
//                filter.getStart(),
//                filter.getEnd(),
//                filter.getSortDirection() == SortDirection.ASC ? 1: -1);


//        return repository.getShippingGeoData(filter.populateNullFields());
    }
}
