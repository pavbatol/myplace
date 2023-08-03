package ru.pavbatol.myplace.dto.shipping;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Map;

@Value
@Builder
@Jacksonized
public class ShippingGeoDtoResponse {
    Long itemId;
    Integer countryCount;
    Integer cityCount;

    String country;

    List<String> countries;
    List<String> cities;
    Map<String, List<String>> countryCities;
}
