package ru.pavbatol.myplace.stats.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.SortDirection;
import ru.pavbatol.myplace.dto.cart.CartItemDtoAddResponse;
import ru.pavbatol.myplace.dto.cart.CartItemDtoAddRequest;
import ru.pavbatol.myplace.dto.cart.CartItemDtoResponse;
import ru.pavbatol.myplace.dto.cart.CartItemSearchFilter;
import ru.pavbatol.myplace.stats.cart.mapper.CartItemMapper;
import ru.pavbatol.myplace.stats.cart.model.CartItem;
import ru.pavbatol.myplace.stats.cart.repository.CartItemMongoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemMongoRepository repository;
    private final CartItemMapper mapper;

    @Override
    public Mono<CartItemDtoAddResponse> add(CartItemDtoAddRequest dto) {
        CartItem entity = mapper.toEntity(dto);
        LocalDateTime dateTime = entity.getTimestamp() == null
                ? LocalDateTime.now() //.truncatedTo(ChronoUnit.SECONDS)
                : entity.getTimestamp(); //.truncatedTo(ChronoUnit.SECONDS);
        entity.setTimestamp(dateTime);
        return repository.save(entity).map(mapper::toAddResponse);
    }

    @Override
    public Flux<CartItemDtoResponse> find(CartItemSearchFilter filter) {
        filter
                .setStart(filter.getStart() != null ? filter.getStart() : LocalDateTime.of(1970, 1, 1, 0, 0, 0))
                .setEnd(filter.getEnd() != null ? filter.getEnd() : LocalDateTime.now())
                .setItemIds(filter.getItemIds() != null ? filter.getItemIds() : List.of())
                .setUnique(filter.getUnique() != null ? filter.getUnique() : false)
                .setSortDirection(filter.getSortDirection() != null ? filter.getSortDirection().name() : SortDirection.DESC.name());
        return repository.find(filter);
    }
}
