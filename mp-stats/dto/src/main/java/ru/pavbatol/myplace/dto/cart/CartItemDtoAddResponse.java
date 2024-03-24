package ru.pavbatol.myplace.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import ru.pavbatol.myplace.dto.annotation.CustomJsonDateTimeFormat;

import java.time.LocalDateTime;

@Value
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CartItemDtoAddResponse {
    String id;
    Long userId;
    Long itemId;
    @CustomJsonDateTimeFormat
    LocalDateTime timestamp;
}
