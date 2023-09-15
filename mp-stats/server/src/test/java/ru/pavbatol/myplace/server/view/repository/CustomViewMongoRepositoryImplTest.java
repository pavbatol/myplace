package ru.pavbatol.myplace.server.view.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import ru.pavbatol.myplace.dto.SortDirection;
import ru.pavbatol.myplace.dto.view.ViewDtoResponse;
import ru.pavbatol.myplace.dto.view.ViewSearchFilter;
import ru.pavbatol.myplace.server.CustomDataMongoTest;
import ru.pavbatol.myplace.server.app.context.ApplicationContextProvider;
import ru.pavbatol.myplace.server.view.model.View;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@CustomDataMongoTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CustomViewMongoRepositoryImplTest {

    private static ViewMongoRepository viewMongoRepository;

    private static final String APP = "test-app";
    private static final String URI = "test-uri";
    private static final String IP = "test-ip";
    private static final LocalDateTime DATE_TIME = LocalDateTime.now().minusMinutes(1);
    private static final int YEARS = 5;

    @BeforeAll
    static void beforeAll(@Autowired ApplicationContextProvider contextProvider) {
        ApplicationContext context = ApplicationContextProvider.getContext();
        assert context != null : "(ApplicationContext) context is null";

        viewMongoRepository = context.getBean(ViewMongoRepository.class);

        viewMongoRepository.deleteAll().block();

        View View = new View().setApp(APP).setUri(URI).setIp(IP).setTimestamp(DATE_TIME.minusYears(YEARS));
        View View0 = new View().setApp(APP + "2").setUri(URI).setIp(IP).setTimestamp(DATE_TIME.minusYears(YEARS));
        View View1 = new View().setApp(APP + "2").setUri(URI).setIp(IP + "2").setTimestamp(DATE_TIME);
        View View2 = new View().setApp(APP).setUri(URI).setIp(IP + "2").setTimestamp(DATE_TIME);
        View View3 = new View().setApp(APP).setUri(URI + "2").setIp(IP + "2").setTimestamp(DATE_TIME);
        View View4 = new View().setApp(APP + "2").setUri(URI + "2").setIp(IP + "2").setTimestamp(DATE_TIME);

        viewMongoRepository.saveAll(List.of(
                View, View, View, View, View, View, View,
                View0, View0, View0,
                View1, View1,
                View2, View2, View2,
                View3, View3, View3, View3,
                View4, View4, View4
        )).collectList().block();
    }

    @AfterAll
    static void afterAll() {
        viewMongoRepository.deleteAll().block();
    }

    @Test
    public void find_shouldReturnWithCorrectViewsFields_whenFilterWithUniqueIsTrueAmdSortIsDescAndIstAsc() {
        ViewSearchFilter filter = ViewSearchFilter.builder().build().setNullFieldsToDefault();
        filter.setUnique(true);
        filter.setSortDirection(SortDirection.DESC.name());

        List<ViewDtoResponse> responses = viewMongoRepository.find(filter).collectList().block();

        assertNotNull(responses);
        assertEquals(4, responses.size());
        assertEquals(2, responses.get(0).getViews(), "Error: get(0).getViews() not equal to 2 if DESC sort.");
        assertEquals(2, responses.get(1).getViews(), "Error: get(1).getViews() not equal to 2 if DESC sort.");
        assertEquals(1, responses.get(2).getViews(), "Error: get(2).getViews() not equal to 1 if DESC sort.");
        assertEquals(1, responses.get(3).getViews(), "Error: get(3).getViews() not equal to 1 if DESC sort.");

        //--
        filter.setSortDirection(SortDirection.ASC.name());

        responses = viewMongoRepository.find(filter).collectList().block();

        assertNotNull(responses);
        assertEquals(4, responses.size(), "Error: The size of 'responses' is not equal to 4.");
        assertEquals(1, responses.get(0).getViews(), "Error: get(0).getViews() not equal to 1 if ASC sort.");
        assertEquals(1, responses.get(1).getViews(), "Error: get(1).getViews() not equal to 1 if ASC sort.");
        assertEquals(2, responses.get(2).getViews(), "Error: get(2).getViews() not equal to 2 if ASC sort.");
        assertEquals(2, responses.get(3).getViews(), "Error: get(3).getViews() not equal to 2 if ASC sort.");
    }

    @Test
    public void find_shouldReturnWithCorrectViewsFields_whenFilterWithUniqueAsFalseOrNull() {
        ViewSearchFilter filterUniqueNull = ViewSearchFilter.builder().build().setNullFieldsToDefault();
        filterUniqueNull.setUnique(null);
        filterUniqueNull.setSortDirection(SortDirection.DESC.name());

        ViewSearchFilter filterUniqueFalse = ViewSearchFilter.builder().build().setNullFieldsToDefault();
        filterUniqueFalse.setUnique(false);
        filterUniqueFalse.setSortDirection(SortDirection.DESC.name());

        List<ViewDtoResponse> responsesByUniqueNull = viewMongoRepository.find(filterUniqueNull).collectList().block();
        List<ViewDtoResponse> responsesByUniqueFalse = viewMongoRepository.find(filterUniqueFalse).collectList().block();

        assertNotNull(responsesByUniqueNull, "Error: responsesByUniqueNull is null.");
        assertNotNull(responsesByUniqueFalse, "Error: responsesByUniqueFalse is null.");

        assertNull(filterUniqueNull.getUnique(), "Error: filterUniqueNull.getUnique() is not null.");
        assertFalse(filterUniqueFalse.getUnique(), "Error: filterUniqueFalse.getUnique() is true.");
        assertEquals(responsesByUniqueNull, responsesByUniqueFalse, "Error: responsesByUniqueNull and responsesByUniqueFalse are not equal.");
        assertEquals(4, responsesByUniqueNull.size(), "Error: The size of 'responsesByUniqueNull' is not equal to 4.");
        assertEquals(10, responsesByUniqueNull.get(0).getViews(), "Error: get(0).getViews() not equal to 10 if DESC sort.");
        assertEquals(5, responsesByUniqueNull.get(1).getViews(), "Error: get(1).getViews() not equal to 5 if DESC sort.");
        assertEquals(4, responsesByUniqueNull.get(2).getViews(), "Error: get(2).getViews() not equal to 4 if DESC sort.");
        assertEquals(3, responsesByUniqueNull.get(3).getViews(), "Error: get(3).getViews() not equal to 3 if DESC sort.");
    }

    @Test
    public void find_shouldReturnWithCorrectViewsFields_whenFilterWithDateRange() {
        ViewSearchFilter filter = ViewSearchFilter.builder().build().setNullFieldsToDefault();
        filter.setStart(DATE_TIME.minusYears(YEARS));
        filter.setEnd(DATE_TIME.minusYears(YEARS));
        filter.setSortDirection(SortDirection.DESC.name());

        List<ViewDtoResponse> responses = viewMongoRepository.find(filter).collectList().block();

        assertNotNull(responses, "Error:  List<ViewDtoResponse> responses is null.");

        assertEquals(2, responses.size(), "Error: The size of 'responses' is not equal to 2.");
        assertEquals(7, responses.get(0).getViews(), "Error: get(0).getViews() not equal to 7 if DESC sort.");
        assertEquals(3, responses.get(1).getViews(), "Error: get(1).getViews() not equal to 3 if DESC sort.");
    }

    @Test
    public void find_shouldReturnWithCorrectViewsFields_whenFilterSpecifiedUris() {
        ViewSearchFilter filter = ViewSearchFilter.builder().build().setNullFieldsToDefault();
        filter.setUris(List.of(URI, URI + "2"));
        filter.setSortDirection(SortDirection.DESC.name());

        List<ViewDtoResponse> responses = viewMongoRepository.find(filter).collectList().block();

        assertNotNull(responses, "Error:  List<ViewDtoResponse> responses is null.");

        assertEquals(4, responses.size(), "Error: The size of 'responses' is not equal to 4.");
        assertEquals(10, responses.get(0).getViews(), "Error: get(0).getViews() not equal to 10 if DESC sort.");
        assertEquals(5, responses.get(1).getViews(), "Error: get(1).getViews() not equal to 5 if DESC sort.");
        assertEquals(4, responses.get(2).getViews(), "Error: get(2).getViews() not equal to 4 if DESC sort.");
        assertEquals(3, responses.get(3).getViews(), "Error: get(3).getViews() not equal to 3 if DESC sort.");

        //--
        filter.setUris(List.of(URI + "2"));

        List<ViewDtoResponse> responses2 = viewMongoRepository.find(filter).collectList().block();

        assertNotNull(responses2, "Error:  List<ViewDtoResponse> responses2 is null.");


        assertEquals(2, responses2.size(), "Error: The size of 'responses2' is not equal to 2.");
        assertEquals(4, responses2.get(0).getViews(), "Error: get(0).getViews() not equal to 4 if DESC sort.");
        assertEquals(3, responses2.get(1).getViews(), "Error: get(1).getViews() not equal to 3 if DESC sort.");
    }

    @Test
    public void find_shouldReturnRightPaginated_whenFilterWithSpecifiedPagination() {
        ViewSearchFilter filter = ViewSearchFilter.builder().build().setNullFieldsToDefault();
        filter.setPageNumber(2);
        filter.setPageSize(2);
        filter.setSortDirection(SortDirection.DESC.name());

        List<ViewDtoResponse> responses = viewMongoRepository.find(filter).collectList().block();

        assertNotNull(responses, "Error:  List<ViewDtoResponse> responses is null.");

        assertEquals(2, responses.size(), "Error: The size of 'responses' is not equal to 2.");
        assertEquals(4, responses.get(0).getViews(), "Error: get(0).getViews() not equal to 4 if DESC sort.");
        assertEquals(3, responses.get(1).getViews(), "Error: get(1).getViews() not equal to 3 if DESC sort.");
    }
}
