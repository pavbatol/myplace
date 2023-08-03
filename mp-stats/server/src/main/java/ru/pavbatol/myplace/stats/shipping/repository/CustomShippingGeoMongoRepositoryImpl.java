package ru.pavbatol.myplace.stats.shipping.repository;

import com.mongodb.BasicDBObject;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
//import org.springframework.data.mongodb.repository.Aggregation;
//import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import ru.pavbatol.myplace.dto.SortDirection;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoResponse;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoSearchFilter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.AggregationFunctionExpressions.*;

@RequiredArgsConstructor
public class CustomShippingGeoMongoRepositoryImpl implements CustomShippingGeoMongoRepository {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<ShippingGeoDtoResponse> findShippingCountryCities(ShippingGeoSearchFilter filter) {
        Sort.Direction direction = filter.getSortDirection() != null && filter.getSortDirection() == SortDirection.ASC
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        MatchOperation betweenDates = match(new Criteria("timestamp").gte(filter.getStart()).lte(filter.getEnd()));
        MatchOperation inItemIds = match(CollectionUtils.isEmpty(filter.getItemIds())
                ? new Criteria() : new Criteria("itemId").in(filter.getItemIds()));
        MatchOperation inCountries = match(CollectionUtils.isEmpty(filter.getCountries())
                ? new Criteria() : new Criteria("country").in(filter.getCountries()));

        GroupOperation groupByCountry = filter.getUnique()
                ? group("itemId", "country").addToSet("city").as("cities")
                : group("itemId", "country").push("city").as("cities");
        GroupOperation groupByItemId = group("itemId").push(
                new BasicDBObject("country", "$_id.country")
                        .append("cities", "$cities")
                        .append("cCount", new BasicDBObject("$size", "$cities"))).as("countries");

        SortOperation sort = Aggregation.sort(direction, "cityCount");

        ProjectionOperation projection = project()
                .andExclude("_id")
                .and("_id").as("itemId")
                .and("countries").size().as("countryCount")
                .andExpression("sum(countries.cCount)").as("cityCount")
                .andExpression(" " +
                        "{ " +
                        "  $arrayToObject: { " +
                        "    $map: { " +
                        "      input: '$countries', " +
                        "      as: 'country', " +
                        "      in: { " +
                        "        k: '$$country.country', " +
                        "        v: '$$country.cities' " +
                        "      } " +
                        "    } " +
                        "  } " +
                        "} "
                ).as("countryCities");

        Aggregation aggregation = Aggregation.newAggregation(
                betweenDates,
                inItemIds,
                inCountries,
                groupByCountry,
                groupByItemId,
                projection,
                sort);

        return reactiveMongoTemplate.aggregate(aggregation, "shippingGeos", ShippingGeoDtoResponse.class);
    }

    @Override
    public Flux<ShippingGeoDtoResponse> getShippingGeoData(ShippingGeoSearchFilter filter) {

        MatchOperation matchDate = match(new Criteria("timestamp").gte(filter.getStart()).lte(filter.getEnd()));
        MatchOperation matchItemId = match(CollectionUtils.isEmpty(filter.getItemIds())
                ? new Criteria() : new Criteria("itemId").in(filter.getItemIds()));
        MatchOperation matchCountry = match(CollectionUtils.isEmpty(filter.getCountries())
                ? new Criteria() : new Criteria("country").in(filter.getCountries()));

        GroupOperation groupByItemId = filter.getUnique()
                ? group("itemId")
                .addToSet("country").as("countries")
                .addToSet("city").as("cities")
                : group("itemId")
                .addToSet("country").as("countries")
                .push("city").as("cities");

        Aggregation aggregation = Aggregation.newAggregation(
                matchDate, matchItemId, matchCountry, groupByItemId,
                project()
                        .andExclude("_id")
                        .and("itemId").previousOperation()
                        .and("countries").as("countries")
                        .and("cities").as("cities")
                        .andExpression("size(countries)").as("countryCount")
                        .andExpression("size(cities)").as("cityCount"),
                sort(Sort.Direction.DESC, "cityCount")

//                group().push(new Document("k", "$country").append("v", "$cities")).as("countryCities")

//                        .and(zip("countries", "cities")).as("countryCities")
//                        .and(mapBuilder()
//                                .sourceAsMap(VariableOperators.Variable.valueOf("countryCities"))
//                                .as("kvp")
//                                .instantiateClass(KeyValue.class)
//                        ).as("countryCities");

//                        .and("countryCities").push(
//                                new BasicDBObject("$arrayToObject",
//                                        new BasicDBObject("$zip", Arrays.asList(
//                                                "$countries", "$cities"
//                                        ))
//                                )
//                        );

//                .and(AccumulatorOperators.AddToSet.addToSet("countryCities").expression(
//                        new BasicDBObject("$arrayToObject",
//                                new BasicDBObject("$zip", Arrays.asList(
//                                        "$countries", "$cities"
//                                ))
//                        )
//                ));

//
        );

        return reactiveMongoTemplate.aggregate(aggregation, "shippingGeos", ShippingGeoDtoResponse.class);
    }


}
