package ru.pavbatol.myplace.dto.cart;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CartItemDtoResponse {
    Long itemId;
    Integer cartItemCount;
}
