package ru.pavbatol.myplace.stats.cart.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import ru.pavbatol.myplace.dto.SortDirection;
import ru.pavbatol.myplace.dto.cart.CartItemDtoResponse;
import ru.pavbatol.myplace.dto.cart.CartItemSearchFilter;

@RequiredArgsConstructor
public class CustomCartItemMongoRepositoryImpl implements CustomCartItemMongoRepository {
    public static final String ITEM_ID = "itemId";
    public static final String TIMESTAMP = "timestamp";
    public static final String CART_ITEM_COUNT = "cartItemCount";
    public static final String USER_ID = "userId";
    public static final String CART_ITEMS = "cartItems";
    public static final String TEMP = "temp";
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<CartItemDtoResponse> find(CartItemSearchFilter filter) {
        Sort.Direction direction = filter.getSortDirection() != null && filter.getSortDirection() == SortDirection.ASC
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        MatchOperation betweenDates = Aggregation.match(new Criteria(TIMESTAMP).gte(filter.getStart()).lte(filter.getEnd()));
        MatchOperation inItemIds = Aggregation.match(CollectionUtils.isEmpty(filter.getItemIds())
                ? new Criteria() : new Criteria(ITEM_ID).in(filter.getItemIds()));
        GroupOperation group = Aggregation.group(ITEM_ID, USER_ID, "-");
        GroupOperation groupAndCount = Aggregation.group(ITEM_ID, "-").addToSet(USER_ID).as(TEMP).count().as(CART_ITEM_COUNT);
        SortOperation sort = Aggregation.sort(direction, CART_ITEM_COUNT);
        ProjectionOperation projection = Aggregation.project(ITEM_ID, CART_ITEM_COUNT);

        Aggregation aggregation = filter.getUnique()
                ? Aggregation.newAggregation(betweenDates, inItemIds, group, groupAndCount, sort, projection)
                : Aggregation.newAggregation(betweenDates, inItemIds, groupAndCount, sort, projection);

        return reactiveMongoTemplate.aggregate(aggregation, CART_ITEMS, CartItemDtoResponse.class);
    }
}
