package ru.pavbatol.myplace.stats.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.cart.*;
import ru.pavbatol.myplace.stats.cart.service.CartItemService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
@Tag(name = "CartItems", description = "API for working with 'CartItem' entity")
public class CartItemController {

    private final CartItemService service;

    @PostMapping("/cartitems")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add", description = "adding a CartItem")
    public Mono<CartItemDtoAddResponse> add(@Valid @RequestBody CartItemDtoAddRequest dto) {
        log.debug("POST (add) with dto={},", dto);
        return service.add(dto);
    }

    @GetMapping("/cartitems")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "find", description = "finding CartItems by filter")
    public Flux<CartItemDtoResponse> find(@Valid CartItemSearchFilter filter) {
        log.debug("GET (find) with filter={}", filter);
        return service.find(filter);
    }

    @GetMapping("/user/cartitems")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "findUserCartItems", description = "finding item IDs in the cart by users")
    public Flux<UserCartItemDtoResponse> findUserCartItems(@Valid UserCartItemSearchFilter filter) {
        log.debug("GET (findUserCartItems) with filter={}", filter);
        return service.findUserCartItems(filter);
    }
}
