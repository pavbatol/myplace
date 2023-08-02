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
import ru.pavbatol.myplace.dto.cart.UserCartItemDtoResponse;
import ru.pavbatol.myplace.dto.cart.UserCartItemSearchFilter;

@RequiredArgsConstructor
public class CustomCartItemMongoRepositoryImpl implements CustomCartItemMongoRepository {
    public static final String ITEM_ID = "itemId";
    public static final String TIMESTAMP = "timestamp";
    public static final String CART_ITEM_COUNT = "cartItemCount";
    public static final String USER_ID = "userId";
    public static final String CART_ITEMS = "cartItems";
    public static final String TEMP = "temp";
    public static final String CART_ITEM_IDS = "cartItemIds";
    public static final String ITEM_COUNT = "itemCount";
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

    @Override
    public Flux<UserCartItemDtoResponse> findUserCartItems(UserCartItemSearchFilter filter) {
        Sort.Direction direction = filter.getSortDirection() != null && filter.getSortDirection() == SortDirection.ASC
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        MatchOperation betweenDates = Aggregation.match(new Criteria(TIMESTAMP).gte(filter.getStart()).lte(filter.getEnd()));

        MatchOperation inUserIds = Aggregation.match(CollectionUtils.isEmpty(filter.getUserIds())
                ? new Criteria() : new Criteria(USER_ID).in(filter.getUserIds()));

        GroupOperation group = filter.getUnique()
                ? Aggregation.group(USER_ID).addToSet(ITEM_ID).as(CART_ITEM_IDS)
                : Aggregation.group(USER_ID).push(ITEM_ID).as(CART_ITEM_IDS);

        ProjectionOperation projection = Aggregation.project(CART_ITEM_IDS)
                .and(CART_ITEM_IDS).size().as(ITEM_COUNT)
                .and(USER_ID).previousOperation();

        SortOperation sort = Aggregation.sort(direction, ITEM_COUNT);

        Aggregation aggregation = CollectionUtils.isEmpty(filter.getUserIds())
                ? Aggregation.newAggregation(betweenDates, group, projection, sort)
                : Aggregation.newAggregation(betweenDates, inUserIds, group, projection, sort);

        return reactiveMongoTemplate.aggregate(aggregation, CART_ITEMS, UserCartItemDtoResponse.class);
    }
}
