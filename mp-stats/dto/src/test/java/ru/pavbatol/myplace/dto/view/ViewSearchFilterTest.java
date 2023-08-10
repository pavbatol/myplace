package ru.pavbatol.myplace.dto.view;

import ru.pavbatol.myplace.dto.SortDirection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ViewSearchFilterTest {

    @org.junit.jupiter.api.Test
    void setNullFieldsToDefault_ShouldCheckingAllFields() {
        LocalDateTime dateTimePast = LocalDateTime.now().minusSeconds(1);
        SortDirection direction = SortDirection.DESC;
        int pageSize = 10;
        int pageNumber = 1;
        int expectedFieldsCount = 3;

        ViewSearchFilter filter = ViewSearchFilter.builder()
                .start(null)
                .end(null)
                .unique(null)
                .sortDirection(null)
                .pageSize(null)
                .pageNumber(null)
                .uris(null)
                .build();

        int actualFieldsCount = filter.getClass().getDeclaredFields().length;

        filter.setNullFieldsToDefault();

        assertAll("Checking fields",
                () -> assertTrue(filter.getStart().isBefore(dateTimePast), "The 'start' field is not before the control date"),
                () -> assertTrue(filter.getEnd().isAfter(dateTimePast), "The 'end' field is not after the control date"),
                () -> assertFalse(filter.getUnique(), "The 'unique' field is not false"),
                () -> assertSame(direction, filter.getSortDirection(), "The 'SortDirection' field is not " + direction),
                () -> assertEquals(pageSize, filter.getPageSize(), "The 'pageSize' field is not equal to " + pageSize),
                () -> assertEquals(pageNumber, filter.getPageNumber(), "The 'pageNumber' field is not equal to " + pageNumber),
                () -> assertEquals(List.of(), filter.getUris(), "Field 'uris' is not an empty list")
        );

        assertEquals(expectedFieldsCount, actualFieldsCount, "The number of fields does not match");
    }

    @org.junit.jupiter.api.Test
    void toQuery_ShouldAllNonNullParametersAreAdded() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String dateTimeStr = "2023-08-10T19:11:00";

        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);
        SortDirection direction = SortDirection.DESC;
        boolean unique = true;
        int pageSize = 10;
        int pageNumber = 1;
        List<String> uris = List.of("/1", "/2");

        ViewSearchFilter filter = ViewSearchFilter.builder()
                .start(dateTime)
                .end(dateTime)
                .unique(unique)
                .sortDirection(direction)
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .uris(uris)
                .build();

        String actualQuery = filter.toQuery(formatter);
        Map<String, String> actualQueryParams = parseQueryParams(actualQuery);

        assertEquals(7, actualQueryParams.size(), "The number of parameters don't match");
        assertEquals(dateTimeStr, actualQueryParams.get("start"), "The 'start' parameter doesn't match");
        assertEquals(dateTimeStr, actualQueryParams.get("end"), "The 'end' parameter doesn't match");
        assertEquals("" + unique, actualQueryParams.get("unique"), "The 'unique' parameter doesn't match");
        assertEquals(direction.name(), actualQueryParams.get("sortDirection"), "The 'sortDirection' parameter doesn't match");
        assertEquals(String.join(",", uris), actualQueryParams.get("uris"), "The 'uris' parameter doesn't match");
        assertEquals(String.valueOf(pageSize), actualQueryParams.get("pageSize"), "The 'pageSize' parameter doesn't match");
        assertEquals(String.valueOf(pageNumber), actualQueryParams.get("pageNumber"), "The 'pageNumber' parameter doesn't match");
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> queryParams = new HashMap<>();

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            String key = idx > 0 ? pair.substring(0, idx) : pair;
            String value = idx > 0 && pair.length() > idx + 1 ? pair.substring(idx + 1) : null;
            queryParams.put(key, value);
        }

        return queryParams;
    }
}