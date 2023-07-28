package ru.pavbatol.myplace.dto.cart;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.rmi.server.UID;

@Value
@Builder
@Jacksonized

//@Value
//@Builder
//@NoArgsConstructor(force = true)
//@AllArgsConstructor
public class CartItemDtoResponse {
//    @With
    Long itemId;
//    @With
    int cartItemCount;
}
