package ru.pavbatol.myplace.server.cart.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.cart.*;

public interface CartItemService {
    Mono<CartItemDtoAddResponse> add(CartItemDtoAddRequest dto);

    Flux<CartItemDtoResponse> find(CartItemSearchFilter filter);

    Flux<UserCartItemDtoResponse> findUserCartItems(UserCartItemSearchFilter filter);
}
