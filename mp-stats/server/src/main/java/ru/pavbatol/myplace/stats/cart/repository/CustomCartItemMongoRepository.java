package ru.pavbatol.myplace.stats.cart.repository;

import reactor.core.publisher.Flux;
import ru.pavbatol.myplace.dto.cart.CartItemDtoResponse;
import ru.pavbatol.myplace.dto.cart.CartItemSearchFilter;

public interface CustomCartItemMongoRepository {
    Flux<CartItemDtoResponse> find(CartItemSearchFilter filter);
}
