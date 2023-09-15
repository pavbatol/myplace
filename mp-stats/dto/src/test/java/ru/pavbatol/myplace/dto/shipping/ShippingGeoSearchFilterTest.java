package ru.pavbatol.myplace.dto.shipping;

import org.junit.jupiter.api.Test;
import org.powermock.api.mockito.PowerMockito;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.pavbatol.myplace.dto.TestHelper.parseQueryParams;

class ShippingGeoSearchFilterTest {

    @Test
    void setNullFieldsToDefault_shouldSettingNullFieldsToDefault() {
        List<Long> itemIds = List.of();
        List<String> countries = List.of();
        Integer lastCityCount = null;
        Integer lastCountryCount = null;
        Long lastItemId = null;
        int expectedFieldsCount = 5;

        ShippingGeoSearchFilter filter = ShippingGeoSearchFilter.builder().build();
        int actualFieldsCount = filter.getClass().getDeclaredFields().length;

        filter.setNullFieldsToDefault();

        assertAll("Checking fields",
                () -> assertEquals(itemIds, filter.getItemIds(), "The 'itemIds' field is not equal to " + itemIds),
                () -> assertEquals(countries, filter.getCountries(), "The 'countries' field is not equal to " + countries),
                () -> assertEquals(lastCityCount, filter.getLastCityCount(), "The 'lastCityCount' field is not equal to " + lastCityCount),
                () -> assertEquals(lastCountryCount, filter.getLastCountryCount(), "The 'lastCountryCount' field is not equal to " + lastCountryCount),
                () -> assertEquals(lastItemId, filter.getLastItemId(), "The 'lastItemId' field is not equal to " + lastItemId)
        );

        assertEquals(expectedFieldsCount, actualFieldsCount, "The number of fields does not match");

        //-- Checking the parent protected method call
        ShippingGeoSearchFilter powerMockitoFilter = PowerMockito.spy(ShippingGeoSearchFilter.builder().build());

        powerMockitoFilter.setNullFieldsToDefault();

        try {
            PowerMockito.verifyPrivate(powerMockitoFilter).invoke("setBaseNullFieldsToDefault");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void toQuery_shouldAllNonNullParametersAreAddedToQuery() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        List<Long> itemIds = List.of(1L, 3L);
        List<String> countries = List.of("RF", "Russian Federation");
        Integer lastCityCount = 5;
        Integer lastCountryCount = 2;
        Long lastItemId = 10L;
        int expectedParamsCount = 5;

        ShippingGeoSearchFilter filter = ShippingGeoSearchFilter.builder()
                .itemIds(itemIds)
                .countries(countries)
                .lastCityCount(lastCityCount)
                .lastCountryCount(lastCountryCount)
                .lastItemId(lastItemId)
                .build();

        String actualQuery = filter.toQuery(formatter);
        Map<String, String> actualQueryParams = parseQueryParams(actualQuery);

        assertEquals(expectedParamsCount, actualQueryParams.size(), "The number of parameters don't match");
        assertEquals(itemIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",")), actualQueryParams.get("itemIds"), "The 'itemIds' parameter doesn't match");
        assertEquals(String.join(",", countries), actualQueryParams.get("countries"), "The 'countries' parameter doesn't match");
        assertEquals(String.valueOf(lastCityCount), actualQueryParams.get("lastCityCount"), "The 'lastCityCount' parameter doesn't match");
        assertEquals(String.valueOf(lastCountryCount), actualQueryParams.get("lastCountryCount"), "The 'lastCountryCount' parameter doesn't match");
        assertEquals(String.valueOf(lastItemId), actualQueryParams.get("lastItemId"), "The 'lastItemId' parameter doesn't match");

        //-- Checking the parent protected method call
        ShippingGeoSearchFilter powerMockitoFilter = PowerMockito.spy(ShippingGeoSearchFilter.builder().build());

        powerMockitoFilter.toQuery(formatter);

        try {
            PowerMockito.verifyPrivate(powerMockitoFilter).invoke("baseFilterToQuery", formatter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}