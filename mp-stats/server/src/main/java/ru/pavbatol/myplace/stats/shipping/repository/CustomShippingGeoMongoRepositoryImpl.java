package ru.pavbatol.myplace.stats.shipping.repository;

import com.mongodb.BasicDBObject;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
//import org.springframework.data.mongodb.repository.Aggregation;
//import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import reactor.core.publisher.Flux;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoResponse;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoSearchFilter;

import java.time.LocalDateTime;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@RequiredArgsConstructor
public class CustomShippingGeoMongoRepositoryImpl implements CustomShippingGeoMongoRepository {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<ShippingGeoDtoResponse> findShippingCountryCities(ShippingGeoSearchFilter filter) {
        Aggregation aggregation = Aggregation.newAggregation(
                // Match stage to filter documents based on criteria
                Aggregation.match(Criteria.where("timestamp").gte(LocalDateTime.now().minusDays(7))),

                // Group stage to group by country and accumulate cities
                group("country")
                        .addToSet("city").as("cities")
                        .count().as("count"),

                // Project stage to reshape the output
                project()
                        .and("_id").as("country")
                        .and("cities").as("cityList")
                        .andExclude("_id")
                        .andInclude("count")
        );

        return reactiveMongoTemplate.aggregate(aggregation, "shippingGeos", ShippingGeoDtoResponse.class);
    }

//    public Flux<ShippingGeoDtoResponse> getShippingGeoDtoResponse() {
//        Aggregation aggregation = Aggregation.newAggregation(
//                group("country")
//                        .addToSet("city").as("cities")
//                        .count().as("count"),
//                group().push(new BasicDBObject("k", "$_id").append("v", "$cities")).as("countryCities")
//                        .sum("$count").as("countryCount")
//                        .sum("cities").as("cityCount"),
//                project()
//                        .andExclude("_id")
//                        .and("countryCount").as("countryCount")
//                        .and("cityCount").as("cityCount")
//                        .and(ArrayOperators.ObjectToArray.itemsOf("countryCities")).as("countryCities"),
//                project()
//                        .and("k").as("country")
//                        .and(ArrayOperators.Map.valuesOf("v")).as("cities"),
//                project()
//                        .andInclude("country")
//                        .and(ArrayOperators.Filter.filter("cities").as("city")
//                                .by(ComparisonOperators.Eq.valueOf("$city.v").equalToValue(true)))
//                        .as("cities")
//        );
//
//        return reactiveMongoTemplate.aggregate(aggregation, "shippingGeos", ShippingGeoDtoResponse.class);
//    }

    public Flux<ShippingGeoDtoResponse> getShippingGeoData() {
        Aggregation aggregation = Aggregation.newAggregation(
                group("country").addToSet("city").as("cities"),
                project().and("_id").as("country").andExclude("_id"),
                group().count().as("countryCount").sum("cities").as("cityCount"),
                project()
                        .and("_id").as("itemId")
                        .and("countryCount").as("countryCount")
                        .and("cityCount").as("cityCount")
                        .and("cities").as("countryCities"),
                project()
                        .andExclude("itemId")
                        .and("countryCities.country").as("itemId")
                        .and("countryCities.cities").as("countryCities")
        );

        return reactiveMongoTemplate.aggregate(aggregation, "shippingGeos", ShippingGeoDtoResponse.class);
    }
}
