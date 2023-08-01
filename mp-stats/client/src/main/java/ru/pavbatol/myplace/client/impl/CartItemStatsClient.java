package ru.pavbatol.myplace.client.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.client.AbstractStatsClient;
import ru.pavbatol.myplace.dto.cart.*;

import java.util.stream.Collectors;

@Slf4j
public class CartItemStatsClient
        extends AbstractStatsClient<CartItemDtoAddRequest, CartItemDtoAddResponse, CartItemSearchFilter, CartItemDtoResponse> {

    private static final String CARTITEMS = "cartitems";
    private static final String USER = "user";
    private static final String USER_IDS = "userIds";
    public static final String ITEM_IDS = "itemIds";

    public CartItemStatsClient(String serverUrl) {
        super(serverUrl);
    }

    @Override
    public Mono<CartItemDtoAddResponse> add(@NonNull CartItemDtoAddRequest dto) {
        String path = UriComponentsBuilder.newInstance()
                .pathSegment(STATS)
                .pathSegment(CARTITEMS)
                .build().toUriString();

        return post(path, dto, CartItemDtoAddResponse.class);
    }

    @Override
    public Flux<CartItemDtoResponse> find(@NonNull CartItemSearchFilter filter) {
//        String path = UriComponentsBuilder.newInstance()
//                .pathSegment(STATS)
//                .pathSegment(CARTITEMS)
//                .queryParam("start", filter.getStart() == null ? "" : filter.getStart().format(formatter))
//                .queryParam("end", filter.getEnd() == null ? "" : filter.getEnd().format(formatter))
//                .queryParam("itemIds", filter.getItemIds() == null ? "" : filter.getItemIds().stream()
//                        .map(String::valueOf).collect(Collectors.joining(",")))
//                .queryParam("unique", filter.getUnique() == null ? "" : filter.getUnique())
//                .queryParam("sortDirection", filter.getSortDirection() == null ? "" : filter.getSortDirection())
//                .build().toUriString();

        String path = UriComponentsBuilder.newInstance()
                .pathSegment(STATS)
                .pathSegment(CARTITEMS)
                .query(filter.toQuery(formatter))
                .build().toUriString();

        return get(path, CartItemDtoResponse.class);
    }

    public Flux<UserCartItemDtoResponse> findUserCartItems(@NonNull UserCartItemSearchFilter filter) {
        String path = UriComponentsBuilder.newInstance()
                .pathSegment(STATS)
                .pathSegment(USER)
                .pathSegment(CARTITEMS)
                .query(filter.toQuery(formatter))
                .build()
                .toUriString();

        return get(path, UserCartItemDtoResponse.class);
    }
}
