package ru.pavbatol.myplace.stats.cart.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.cart.CartItemDtoAddResponse;
import ru.pavbatol.myplace.dto.cart.CartItemDtoAddRequest;
import ru.pavbatol.myplace.dto.cart.CartItemDtoResponse;
import ru.pavbatol.myplace.dto.cart.CartItemSearchFilter;

public interface CartItemService {
    Mono<CartItemDtoAddResponse> add(CartItemDtoAddRequest dto);

    Flux<CartItemDtoResponse> find(CartItemSearchFilter filter);
}
