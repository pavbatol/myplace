package ru.pavbatol.myplace.dto.view;

import org.powermock.api.mockito.PowerMockito;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.pavbatol.myplace.dto.TestHelper.parseQueryParams;

class ViewSearchFilterTest {

    @org.junit.jupiter.api.Test
    void setNullFieldsToDefault_ShouldSettingNullFieldsToDefault() {
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
                () -> assertEquals(pageNumber, filter.getPageNumber(), "The 'pageNumber' field is not equal to " + pageNumber),
                () -> assertEquals(List.of(), filter.getUris(), "Field 'uris' is not an empty list")
        );

        assertEquals(expectedFieldsCount, actualFieldsCount, "The number of fields does not match");

        //-- Checking the parent protected method call
        ViewSearchFilter powerMockitoFilter = PowerMockito.spy(ViewSearchFilter.builder().build());

        powerMockitoFilter.setNullFieldsToDefault();

        try {
            PowerMockito.verifyPrivate(powerMockitoFilter).invoke("setBaseNullFieldsToDefault");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void toQuery_ShouldAllNonNullParametersAreAddedToQuery() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        int pageNumber = 1;
        List<String> uris = List.of("/1", "/2");

        ViewSearchFilter filter = ViewSearchFilter.builder()
                .pageNumber(pageNumber)
                .uris(uris)
                .build();

        String actualQuery = filter.toQuery(formatter);
        Map<String, String> actualQueryParams = parseQueryParams(actualQuery);

        assertEquals(2, actualQueryParams.size(), "The number of parameters don't match");
        assertEquals(String.valueOf(pageNumber), actualQueryParams.get("pageNumber"), "The 'pageNumber' parameter doesn't match");
        assertEquals(String.join(",", uris), actualQueryParams.get("uris"), "The 'uris' parameter doesn't match");

        //-- Checking the parent protected method call
        ViewSearchFilter powerMockitoFilter = PowerMockito.spy(ViewSearchFilter.builder().build());

        powerMockitoFilter.toQuery(formatter);

        try {
            PowerMockito.verifyPrivate(powerMockitoFilter).invoke("baseFilterToQuery", formatter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}