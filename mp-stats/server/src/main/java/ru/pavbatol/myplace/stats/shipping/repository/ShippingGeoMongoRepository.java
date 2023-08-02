package ru.pavbatol.myplace.stats.shipping.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoResponse;
import ru.pavbatol.myplace.stats.shipping.model.ShippingGeo;

import java.util.List;

@Repository
public interface ShippingGeoMongoRepository extends ReactiveMongoRepository<ShippingGeo, String>, CustomShippingGeoMongoRepository {
    @Aggregation(pipeline = {
            "{$group: {_id: '$country', cities: {$addToSet: '$city'}}}",
            "{$project: {country: '$_id', cities: 1}}",
            "{$group: {_id: null, countryCities: {$push: {k: '$country', v: '$cities'}}}}",
            "{$addFields: {countryCount: {$size: '$countryCities'}}, cityCount: {$sum: {$map: {input: '$countryCities', as: 'cc', in: {$size: '$$cc.v'}}}}}}",
            "{$project: {_id: 0, countryCities: 1, countryCount: 1, cityCount: 1}}"
    })
    List<ShippingGeoDtoResponse> getShippingGeoData();
}
