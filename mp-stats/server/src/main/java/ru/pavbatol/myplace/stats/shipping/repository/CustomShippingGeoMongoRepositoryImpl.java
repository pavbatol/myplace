package ru.pavbatol.myplace.stats.shipping.repository;

import com.mongodb.BasicDBObject;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import ru.pavbatol.myplace.dto.SortDirection;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoResponse;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoSearchFilter;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

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

        SortOperation sort = Aggregation.sort(direction, "cityCount", "countryCount");

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
}
