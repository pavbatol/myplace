package ru.pavbatol.myplace.dto.cart;

import org.junit.jupiter.api.Test;
import org.powermock.api.mockito.PowerMockito;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static ru.pavbatol.myplace.dto.TestHelper.parseQueryParams;

class UserCartItemSearchFilterTest {

    @Test
    void setNullFieldsToDefault_shouldSettingNullFieldsToDefault() {
        List<Long> userIds = List.of();
        Long lastUserId = null;
        Long lastItemCount = null;
        int expectedFieldsCount = 3;

        UserCartItemSearchFilter filter = UserCartItemSearchFilter.builder().build();
        int actualFieldsCount = filter.getClass().getDeclaredFields().length;

        filter.setNullFieldsToDefault();

        assertAll("Checking fields",
                () -> assertEquals(userIds, filter.getUserIds(), "The 'userIds' field is not equal to " + userIds),
                () -> assertEquals(lastUserId, filter.getLastUserId(), "The 'lastUserId' field is not equal to " + lastUserId),
                () -> assertEquals(lastItemCount, filter.getLastItemCount(), "The 'lastItemCount' field is not equal to " + lastItemCount)
        );

        assertEquals(expectedFieldsCount, actualFieldsCount, "The number of fields does not match");

        //-- Checking the parent protected method call
        UserCartItemSearchFilter powerMockitoFilter = PowerMockito.spy(UserCartItemSearchFilter.builder().build());

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

        List<Long> userIds = List.of(1L, 3L);
        Long lastUserId = 10L;
        Long lastItemCount = 5L;
        int expectedParamsCount = 3;

        UserCartItemSearchFilter filter = UserCartItemSearchFilter.builder()
                .userIds(userIds)
                .lastUserId(lastUserId)
                .lastItemCount(lastItemCount)
                .build();

        String actualQuery = filter.toQuery(formatter);
        Map<String, String> actualQueryParams = parseQueryParams(actualQuery);

        assertEquals(expectedParamsCount, actualQueryParams.size(), "The number of parameters don't match");
        assertEquals(userIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",")), actualQueryParams.get("userIds"), "The 'userIds' parameter doesn't match");
        assertEquals(String.valueOf(lastUserId), actualQueryParams.get("lastUserId"), "The 'lastUserId' parameter doesn't match");
        assertEquals(String.valueOf(lastItemCount), actualQueryParams.get("lastItemCount"), "The 'lastItemCount' parameter doesn't match");

        //-- Checking the parent protected method call
        UserCartItemSearchFilter powerMockitoFilter = PowerMockito.spy(UserCartItemSearchFilter.builder().build());

        powerMockitoFilter.toQuery(formatter);

        try {
            PowerMockito.verifyPrivate(powerMockitoFilter).invoke("baseFilterToQuery", formatter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}