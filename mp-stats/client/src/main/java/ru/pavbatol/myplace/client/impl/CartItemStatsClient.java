package ru.pavbatol.myplace.client.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.client.AbstractStatsClient;
import ru.pavbatol.myplace.dto.cart.*;

@Slf4j
public class CartItemStatsClient
        extends AbstractStatsClient<CartItemDtoAddRequest, CartItemDtoAddResponse, CartItemSearchFilter, CartItemDtoResponse> {

    private static final String CARTITEMS = "cartitems";
    private static final String USER = "user";

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
