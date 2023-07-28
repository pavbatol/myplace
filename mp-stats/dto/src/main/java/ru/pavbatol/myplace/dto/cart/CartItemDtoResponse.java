package ru.pavbatol.myplace.dto.cart;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.rmi.server.UID;

@Value
@Builder
@Jacksonized
public class CartItemDtoResponse {
    Long itemId;
    int cartItemCount;
}
