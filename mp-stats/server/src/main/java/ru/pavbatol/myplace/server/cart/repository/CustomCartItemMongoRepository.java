package ru.pavbatol.myplace.server.cart.repository;

import reactor.core.publisher.Flux;
import ru.pavbatol.myplace.dto.cart.CartItemDtoResponse;
import ru.pavbatol.myplace.dto.cart.CartItemSearchFilter;
import ru.pavbatol.myplace.dto.cart.UserCartItemDtoResponse;
import ru.pavbatol.myplace.dto.cart.UserCartItemSearchFilter;

public interface CustomCartItemMongoRepository {
    Flux<CartItemDtoResponse> find(CartItemSearchFilter filter);

    Flux<UserCartItemDtoResponse> findUserCartItems(UserCartItemSearchFilter filter);
}
