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

        MatchOperation betweenDates = match(new Criteria(TIMESTAMP).gte(filter.getStart()).lte(filter.getEnd()));
        MatchOperation inItemIds = match(CollectionUtils.isEmpty(filter.getItemIds())
                ? new Criteria() : new Criteria(ITEM_ID).in(filter.getItemIds()));
        MatchOperation inCountries = match(CollectionUtils.isEmpty(filter.getCountries())
                ? new Criteria() : new Criteria(COUNTRY).in(filter.getCountries()));

        GroupOperation groupByCountry = filter.getUnique()
                ? group(ITEM_ID, COUNTRY).addToSet(CITY).as(CITIES)
                : group(ITEM_ID, COUNTRY).push(CITY).as(CITIES);
        GroupOperation groupByItemId = group(ITEM_ID).push(
                new BasicDBObject(COUNTRY, "$_id." + COUNTRY)
                        .append(CITIES, "$" + CITIES)
                        .append(C_COUNT, new BasicDBObject("$size", "$" + CITIES))).as(COUNTRIES);

        SortOperation sort = Aggregation.sort(direction, CITY_COUNT, COUNTRY_COUNT);

        SkipOperation skip = new SkipOperation((long) (filter.getPageNumber() - 1) * filter.getPageSize());
        LimitOperation limit = new LimitOperation(filter.getPageSize());

        ProjectionOperation projection = project()
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
                ).as(COUNTRY_CITIES);

        Aggregation aggregation = Aggregation.newAggregation(
                betweenDates,
                inItemIds,
                inCountries,
                groupByCountry,
                groupByItemId,
                projection,
                sort,
                skip,
                limit);

        return reactiveMongoTemplate.aggregate(aggregation, SHIPPING_GEOS, ShippingGeoDtoResponse.class);
    }
}
