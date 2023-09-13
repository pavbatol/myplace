package ru.pavbatol.myplace.dto.shipping;

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
public class ShippingGeoDtoAddResponse {
    String id;
    Long itemId;
    String country;
    String city;
    @CustomJsonDateTimeFormat
    LocalDateTime timestamp;
}
