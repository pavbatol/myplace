package ru.pavbatol.myplace.server.cart.repository;

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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

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

        List<AggregationOperation> aggregations = new ArrayList<>();

        aggregations.add(match(new Criteria(TIMESTAMP).gte(filter.getStart()).lte(filter.getEnd())));

        if (!CollectionUtils.isEmpty(filter.getItemIds())) {
            aggregations.add(match(new Criteria(ITEM_ID).in(filter.getItemIds())));
        }

        if (filter.getUnique() != null && filter.getUnique()) {
            aggregations.add(group(ITEM_ID, USER_ID, "-"));
        }

        aggregations.add(group(ITEM_ID, "-").addToSet(USER_ID).as(TEMP).count().as(CART_ITEM_COUNT));

        aggregations.add(project(ITEM_ID, CART_ITEM_COUNT));

        // KeySet pagination
        Long lastItemId = filter.getLastItemId();
        Integer lastCartItemCount = filter.getLastCartItemCount();
        if (lastItemId != null && lastCartItemCount != null) {
            if (direction == Sort.Direction.DESC) {
                aggregations.add(match(new Criteria().orOperator(
                        new Criteria(CART_ITEM_COUNT).lt(lastCartItemCount),
                        new Criteria(CART_ITEM_COUNT).lte(lastCartItemCount).and(ITEM_ID).lt(lastItemId))));
            } else {
                aggregations.add(match(new Criteria().orOperator(
                        new Criteria(CART_ITEM_COUNT).gt(lastCartItemCount),
                        new Criteria(CART_ITEM_COUNT).gte(lastCartItemCount).and(ITEM_ID).gt(lastItemId))));
            }
        }

        aggregations.add(sort(direction, CART_ITEM_COUNT, ITEM_ID));

        aggregations.add(new LimitOperation(filter.getPageSize()));

        Aggregation aggregation = Aggregation.newAggregation(aggregations);

        return reactiveMongoTemplate.aggregate(aggregation, CART_ITEMS, CartItemDtoResponse.class);
    }

    @Override
    public Flux<UserCartItemDtoResponse> findUserCartItems(UserCartItemSearchFilter filter) {
        Sort.Direction direction = filter.getSortDirection() != null && filter.getSortDirection() == SortDirection.ASC
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        List<AggregationOperation> aggregations = new ArrayList<>();

        aggregations.add(match(new Criteria(TIMESTAMP).gte(filter.getStart()).lte(filter.getEnd())));

        if (!CollectionUtils.isEmpty(filter.getUserIds())) {
            aggregations.add(match(new Criteria(USER_ID).in(filter.getUserIds())));
        }

        aggregations.add(filter.getUnique() != null && filter.getUnique()
                ? group(USER_ID).addToSet(ITEM_ID).as(CART_ITEM_IDS)
                : group(USER_ID).push(ITEM_ID).as(CART_ITEM_IDS));

        aggregations.add(project(CART_ITEM_IDS)
                .and(CART_ITEM_IDS).size().as(ITEM_COUNT)
                .and(USER_ID).previousOperation());

        // KeySet pagination
        Long lastUserId = filter.getLastUserId();
        Long lastItemCount = filter.getLastItemCount();
        if (lastUserId != null && lastItemCount != null) {
            if (direction == Sort.Direction.DESC) {
                aggregations.add(match(new Criteria().orOperator(
                        new Criteria(ITEM_COUNT).lt(lastItemCount),
                        new Criteria(ITEM_COUNT).lte(lastItemCount).and(USER_ID).lt(lastUserId))));
            } else {
                aggregations.add(match(new Criteria().orOperator(
                        new Criteria(ITEM_COUNT).gt(lastItemCount),
                        new Criteria(ITEM_COUNT).gte(lastItemCount).and(USER_ID).gt(lastUserId))));
            }
        }

        aggregations.add(sort(direction, ITEM_COUNT, USER_ID));

        aggregations.add(new LimitOperation(filter.getPageSize()));

        Aggregation aggregation = Aggregation.newAggregation(aggregations);

        return reactiveMongoTemplate.aggregate(aggregation, CART_ITEMS, UserCartItemDtoResponse.class);
    }
}
