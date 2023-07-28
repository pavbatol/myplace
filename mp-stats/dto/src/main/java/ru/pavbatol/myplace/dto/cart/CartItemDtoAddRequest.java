package ru.pavbatol.myplace.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import ru.pavbatol.myplace.dto.annotation.CustomJsonDateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CartItemDtoAddRequest {
    @NotNull
    Long userId;

    @NotNull
    Long itemId;

    @CustomJsonDateTimeFormat
    LocalDateTime timestamp;
}
