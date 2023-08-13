package ru.pavbatol.myplace.server.shipping.repository;

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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RequiredArgsConstructor
public class CustomShippingGeoMongoRepositoryImpl implements CustomShippingGeoMongoRepository {
    public static final String TIMESTAMP = "timestamp";
    public static final String ITEM_ID = "itemId";
    public static final String COUNTRY = "country";
    public static final String CITY = "city";
    public static final String CITIES = "cities";
    public static final String COUNTRIES = "countries";
    public static final String C_COUNT = "cCount";
    public static final String CITY_COUNT = "cityCount";
    public static final String COUNTRY_COUNT = "countryCount";
    public static final String COUNTRY_CITIES = "countryCities";
    public static final String SHIPPING_GEOS = "shippingGeos";
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<ShippingGeoDtoResponse> findShippingCountryCities(ShippingGeoSearchFilter filter) {
        Sort.Direction direction = filter.getSortDirection() != null && filter.getSortDirection() == SortDirection.ASC
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        List<AggregationOperation> aggregations = new ArrayList<>();

        aggregations.add(match(new Criteria(TIMESTAMP).gte(filter.getStart()).lte(filter.getEnd())));

        if (!CollectionUtils.isEmpty(filter.getItemIds())) {
            aggregations.add(match(new Criteria(ITEM_ID).in(filter.getItemIds())));
        }

        if (!CollectionUtils.isEmpty(filter.getCountries())) {
            aggregations.add(match(new Criteria(COUNTRY).in(filter.getCountries())));
        }

        aggregations.add(filter.getUnique()
                ? group(ITEM_ID, COUNTRY).addToSet(CITY).as(CITIES)
                : group(ITEM_ID, COUNTRY).push(CITY).as(CITIES));

        aggregations.add(group(ITEM_ID).push(
                new BasicDBObject(COUNTRY, "$_id." + COUNTRY)
                        .append(CITIES, "$" + CITIES)
                        .append(C_COUNT, new BasicDBObject("$size", "$" + CITIES))).as(COUNTRIES));

        aggregations.add(project()
                .andExclude("_id")
                .and("_id").as(ITEM_ID)
                .and(COUNTRIES).size().as(COUNTRY_COUNT)
                .andExpression("sum(" + COUNTRIES + "." + C_COUNT + ")").as(CITY_COUNT)
                .andExpression(" " +
                        "{ " +
                        "  $arrayToObject: { " +
                        "    $map: { " +
                        "      input: '$" + COUNTRIES + "', " +
                        "      as: '" + COUNTRY + "', " +
                        "      in: { " +
                        "        k: '$$" + COUNTRY + "." + COUNTRY + "', " +
                        "        v: '$$" + COUNTRY + "." + CITIES + "' " +
                        "      } " +
                        "    } " +
                        "  } " +
                        "} "
                ).as(COUNTRY_CITIES));

        // KeySet pagination
        Integer lastCityCount = filter.getLastCityCount();
        Integer lastCountryCount = filter.getLastCountryCount();
        Long lastItemId = filter.getLastItemId();
        if (lastCityCount != null && lastCountryCount != null && lastItemId != null) {
            if (direction == Sort.Direction.DESC) {
                aggregations.add(match(new Criteria().orOperator(
                        new Criteria(CITY_COUNT).lt(lastCityCount),
                        new Criteria(CITY_COUNT).lte(lastCityCount).and(COUNTRY_COUNT).lt(lastCountryCount),
                        new Criteria(CITY_COUNT).lte(lastCityCount).and(COUNTRY_COUNT).lte(lastCountryCount)
                                .and(ITEM_ID).lt(lastItemId))));
            } else {
                aggregations.add(match(new Criteria().orOperator(
                        new Criteria(CITY_COUNT).gt(lastCityCount),
                        new Criteria(CITY_COUNT).gte(lastCityCount).and(COUNTRY_COUNT).gt(lastCountryCount),
                        new Criteria(CITY_COUNT).gte(lastCityCount).and(COUNTRY_COUNT).gte(lastCountryCount)
                                .and(ITEM_ID).gt(lastItemId))));
            }
        }

        aggregations.add(sort(direction, CITY_COUNT, COUNTRY_COUNT, ITEM_ID));

        aggregations.add(new LimitOperation(filter.getPageSize()));

        Aggregation aggregation = Aggregation.newAggregation(aggregations);

        return reactiveMongoTemplate.aggregate(aggregation, SHIPPING_GEOS, ShippingGeoDtoResponse.class);
    }
}
