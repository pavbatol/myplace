package ru.pavbatol.myplace.server.cart.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.pavbatol.myplace.server.cart.model.CartItem;

@Repository
public interface CartItemMongoRepository extends ReactiveMongoRepository<CartItem, String>, CustomCartItemMongoRepository {

}
