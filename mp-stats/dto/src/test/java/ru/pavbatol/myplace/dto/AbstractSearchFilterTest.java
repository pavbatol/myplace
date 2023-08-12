package ru.pavbatol.myplace.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static ru.pavbatol.myplace.dto.TestHelper.parseQueryParams;

class AbstractSearchFilterTest {

    private AbstractSearchFilter filter;
    @BeforeEach
    void setUp() {
        filter = new AbstractSearchFilter() {
            @Override
            public AbstractSearchFilter setNullFieldsToDefault() {
                return null;
            }

            @Override
            public String toQuery(DateTimeFormatter formatter) {
                return null;
            }
        };
    }

    @Test
    void setSortDirection_shouldSortSetFromString() {
        String name = SortDirection.ASC.name();

        filter.setSortDirection(name);

        assertEquals(name, filter.getSortDirection().name(), "Not correct setting 'sortDirection");
    }

    @Test
    void setBaseNullFieldsToDefault_shouldSettingNullFieldsToDefault() {
        LocalDateTime dateTimePast = LocalDateTime.now().minusSeconds(1);
        SortDirection direction = SortDirection.DESC;
        int pageSize = 10;

        filter.setBaseNullFieldsToDefault();

        assertAll("Checking fields",
                () -> assertTrue(filter.getStart().isBefore(dateTimePast), "The 'start' field is not before the control date"),
                () -> assertTrue(filter.getEnd().isAfter(dateTimePast), "The 'end' field is not after the control date"),
                () -> assertFalse(filter.getUnique(), "The 'unique' field is not false"),
                () -> assertSame(direction, filter.getSortDirection(), "The 'SortDirection' field is not " + direction),
                () -> assertEquals(pageSize, filter.getPageSize(), "The 'pageSize' field is not equal to " + pageSize)
        );
    }

    @Test
    void baseFilterToQuery_shouldAllNonNullParametersAreAddedToQuery() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String dateTimeStr = "2023-08-10T19:11:00";

        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);
        SortDirection direction = SortDirection.DESC;
        boolean unique = true;
        int pageSize = 10;
        List<String> uris = List.of("/1", "/2");

        filter.setStart(dateTime);
        filter.setEnd(dateTime);
        filter.setUnique(unique);
        filter.setSortDirection(direction.name());
        filter.setPageSize(pageSize);

        String actualQuery = filter.baseFilterToQuery(formatter);
        Map<String, String> actualQueryParams = parseQueryParams(actualQuery);

        assertEquals(5, actualQueryParams.size(), "The number of parameters don't match");
        assertEquals(dateTimeStr, actualQueryParams.get("start"), "The 'start' parameter doesn't match");
        assertEquals(dateTimeStr, actualQueryParams.get("end"), "The 'end' parameter doesn't match");
        assertEquals("" + unique, actualQueryParams.get("unique"), "The 'unique' parameter doesn't match");
        assertEquals(direction.name(), actualQueryParams.get("sortDirection"), "The 'sortDirection' parameter doesn't match");
        assertEquals(String.valueOf(pageSize), actualQueryParams.get("pageSize"), "The 'pageSize' parameter doesn't match");
    }
}