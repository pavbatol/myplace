package ru.pavbatol.myplace.dto.cart;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class UserCartItemDtoResponse {
    String id;
    Long userId;
    List<Long> cartItemIds;
    Long itemCount;
}
