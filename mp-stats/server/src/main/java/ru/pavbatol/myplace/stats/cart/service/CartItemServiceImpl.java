package ru.pavbatol.myplace.stats.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.cart.*;
import ru.pavbatol.myplace.stats.cart.mapper.CartItemMapper;
import ru.pavbatol.myplace.stats.cart.model.CartItem;
import ru.pavbatol.myplace.stats.cart.repository.CartItemMongoRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemMongoRepository repository;
    private final CartItemMapper mapper;

    @Override
    public Mono<CartItemDtoAddResponse> add(CartItemDtoAddRequest dto) {
        CartItem entity = mapper.toEntity(dto);
        LocalDateTime dateTime = entity.getTimestamp() == null
                ? LocalDateTime.now()
                : entity.getTimestamp();
        entity.setTimestamp(dateTime);
        return repository.save(entity).map(mapper::toDtoAddResponse);
    }

    @Override
    public Flux<CartItemDtoResponse> find(CartItemSearchFilter filter) {
        return repository.find(filter.populateNullFields());
    }

    @Override
    public Flux<UserCartItemDtoResponse> findUserCartItems(UserCartItemSearchFilter filter) {
        return repository.findUserCartItems(filter.populateNullFields());
    }
}
