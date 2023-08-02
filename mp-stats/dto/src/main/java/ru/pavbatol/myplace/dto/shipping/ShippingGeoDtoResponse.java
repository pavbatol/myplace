package ru.pavbatol.myplace.dto.shipping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ShippingGeoDtoResponse {
    Long itemId;
    Map<String, List<String>> countryCities;
    Integer countryCount;
    Integer cityCount;
}
