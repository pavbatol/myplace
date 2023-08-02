package ru.pavbatol.myplace.dto.shipping;

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
public class ShippingGeoDtoAddRequest {
    @NotNull
    Long itemId;

    @NotNull
    String country;

    @NotNull
    String city;

    @CustomJsonDateTimeFormat
    LocalDateTime timestamp;
}
