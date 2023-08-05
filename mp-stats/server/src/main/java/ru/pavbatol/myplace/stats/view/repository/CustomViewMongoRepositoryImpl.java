package ru.pavbatol.myplace.stats.view.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import ru.pavbatol.myplace.dto.SortDirection;
import ru.pavbatol.myplace.dto.view.ViewDtoResponse;
import ru.pavbatol.myplace.dto.view.ViewSearchFilter;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CustomViewMongoRepositoryImpl implements CustomViewMongoRepository {
    public static final String APP = "app";
    public static final String URI = "uri";
    public static final String VIEWS = "views";
    public static final String TIMESTAMP = "timestamp";
    public static final String IP = "ip";
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<ViewDtoResponse> find(@NonNull ViewSearchFilter filter) {
        Sort.Direction direction = filter.getSortDirection() != null && filter.getSortDirection() == SortDirection.ASC
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        List<AggregationOperation> operations = new ArrayList<>();

        operations.add(Aggregation.match(Criteria.where(TIMESTAMP).gte(filter.getStart()).lte(filter.getEnd())));
        if (!CollectionUtils.isEmpty(filter.getUris())) {
            operations.add(Aggregation.match(new Criteria(URI).in(filter.getUris())));
        }
        if (filter.getUnique()) {
            operations.add(Aggregation.group(APP, URI, IP));
        }
        operations.add(Aggregation.group(APP, URI).count().as(VIEWS));
        operations.add(Aggregation.sort(direction, VIEWS));
        operations.add(Aggregation.project(APP, URI, VIEWS));
        operations.add(new SkipOperation((long) (filter.getPageNumber() - 1) * filter.getPageSize()));
        operations.add(new LimitOperation(filter.getPageSize()));

        Aggregation aggregation = Aggregation.newAggregation(operations);

        return reactiveMongoTemplate.aggregate(aggregation, VIEWS, ViewDtoResponse.class);
    }
}
