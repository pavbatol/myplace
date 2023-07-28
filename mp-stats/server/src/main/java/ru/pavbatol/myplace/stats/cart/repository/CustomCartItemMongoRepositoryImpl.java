package ru.pavbatol.myplace.stats.cart.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import reactor.core.publisher.Flux;
import ru.pavbatol.myplace.dto.SortDirection;
import ru.pavbatol.myplace.dto.cart.CartItemDtoResponse;
import ru.pavbatol.myplace.dto.cart.CartItemSearchFilter;

@RequiredArgsConstructor
public class CustomCartItemMongoRepositoryImpl implements CustomCartItemMongoRepository {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<CartItemDtoResponse> find(CartItemSearchFilter filter) {
        Sort.Direction direction = filter.getSortDirection() != null && filter.getSortDirection() == SortDirection.ASC
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        MatchOperation betweenDates = Aggregation.match(new Criteria("start").gte(filter.getStart()).lte(filter.getEnd()));
        MatchOperation inItemIds = Aggregation.match(new Criteria("itemIds").in(filter.getItemIds()));
        GroupOperation group = Aggregation.group("itemId", "userId");
        GroupOperation groupAndCount = Aggregation.group("itemId").count().as("cartItemCount");
        SortOperation sort = Aggregation.sort(direction, "cartItemCount");
        ProjectionOperation projection = Aggregation.project("itemId", "cartItemCount");

        Aggregation aggregation = filter.getUnique()
                ? Aggregation.newAggregation(betweenDates, inItemIds, group, groupAndCount, sort, projection)
                : Aggregation.newAggregation(betweenDates, inItemIds, groupAndCount, sort, projection);

        return reactiveMongoTemplate.aggregate(aggregation, "cartItems", CartItemDtoResponse.class);
    }
}
