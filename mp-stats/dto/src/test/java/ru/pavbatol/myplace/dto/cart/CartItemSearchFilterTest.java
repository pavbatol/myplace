package ru.pavbatol.myplace.dto.cart;

import org.junit.jupiter.api.Test;
import org.powermock.api.mockito.PowerMockito;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.pavbatol.myplace.dto.TestHelper.parseQueryParams;

class CartItemSearchFilterTest {

    @Test
    void setNullFieldsToDefault_shouldSettingNullFieldsToDefault() {
        List<Long> itemIds = List.of();
        Long lastItemId = null;
        Integer lastCartItemCount = null;
        int expectedFieldsCount = 3;

        CartItemSearchFilter filter = CartItemSearchFilter.builder().build();
        int actualFieldsCount = filter.getClass().getDeclaredFields().length;

        filter.setNullFieldsToDefault();

        assertAll("Checking fields",
                () -> assertEquals(itemIds, filter.getItemIds(), "The 'itemIds' field is not equal to " + itemIds),
                () -> assertEquals(lastItemId, filter.getLastItemId(), "The 'lastItemId' field is not equal to " + lastItemId),
                () -> assertEquals(lastCartItemCount, filter.getLastCartItemCount(), "The 'lastCartItemCount' field is not equal to " + lastCartItemCount)
        );

        assertEquals(expectedFieldsCount, actualFieldsCount, "The number of fields does not match");

        //-- Checking the parent protected method call
        CartItemSearchFilter powerMockitoFilter = PowerMockito.spy(CartItemSearchFilter.builder().build());

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
        Long lastItemId = 10L;
        Integer lastCartItemCount = 5;
        int expectedParamsCount = 3;

        CartItemSearchFilter filter = CartItemSearchFilter.builder()
                .itemIds(itemIds)
                .lastItemId(lastItemId)
                .lastCartItemCount(lastCartItemCount)
                .build();

        String actualQuery = filter.toQuery(formatter);
        Map<String, String> actualQueryParams = parseQueryParams(actualQuery);

        assertEquals(expectedParamsCount, actualQueryParams.size(), "The number of parameters don't match");
        assertEquals(itemIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",")), actualQueryParams.get("itemIds"), "The 'itemIds' parameter doesn't match");
        assertEquals(String.valueOf(lastItemId), actualQueryParams.get("lastItemId"), "The 'lastItemId' parameter doesn't match");
        assertEquals(String.valueOf(lastCartItemCount), actualQueryParams.get("lastCartItemCount"), "The 'lastCartItemCount' parameter doesn't match");

        //-- Checking the parent protected method call
        CartItemSearchFilter powerMockitoFilter = PowerMockito.spy(CartItemSearchFilter.builder().build());

        powerMockitoFilter.toQuery(formatter);

        try {
            PowerMockito.verifyPrivate(powerMockitoFilter).invoke("baseFilterToQuery", formatter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}