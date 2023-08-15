package ru.pavbatol.myplace.server.view.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import ru.pavbatol.myplace.dto.SortDirection;
import ru.pavbatol.myplace.dto.view.ViewDtoResponse;
import ru.pavbatol.myplace.dto.view.ViewSearchFilter;
import ru.pavbatol.myplace.server.app.context.ApplicationContextProvider;
import ru.pavbatol.myplace.server.view.model.View;

import java.time.LocalDateTime;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@DataMongoTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {ApplicationContextProvider.class}))
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CustomViewMongoRepositoryImplTest {

    private static ViewMongoRepository viewMongoRepository;

    @BeforeAll
    static void beforeAll(@Autowired ApplicationContextProvider contextProvider) {
        ApplicationContext context = ApplicationContextProvider.getContext();
        assert context != null : "(ApplicationContext) context is null";

        viewMongoRepository = context.getBean(ViewMongoRepository.class);

        String app = "test-app";
        String uri = "test-uri";
        String ip = "test-ip";
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);

        View View = new View().setApp(app).setUri(uri).setIp(ip).setTimestamp(dateTime);
        View View0 = new View().setApp(app + "2").setUri(uri).setIp(ip).setTimestamp(dateTime);
        View View1 = new View().setApp(app + "2").setUri(uri).setIp(ip + "2").setTimestamp(dateTime);
        View View2 = new View().setApp(app).setUri(uri).setIp(ip + "2").setTimestamp(dateTime);
        View View3 = new View().setApp(app).setUri(uri + "2").setIp(ip + "2").setTimestamp(dateTime);
        View View4 = new View().setApp(app + "2").setUri(uri + "2").setIp(ip + "2").setTimestamp(dateTime);

        viewMongoRepository.saveAll(List.of(
                View0, View0, View0,
                View, View, View, View, View, View, View,
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
    public void find_shouldReturnWithCorrectViewsFields_whenAddedWithUniqueIsTrueAmdSortIsDescAndIstAsc() {
        ViewSearchFilter filter = ViewSearchFilter.builder().build().setNullFieldsToDefault();
        filter.setUnique(true);
        filter.setSortDirection(SortDirection.DESC.name());

        List<ViewDtoResponse> responses = viewMongoRepository.find(filter).collectList().block();

        /////////////////////////////////////////////////
//        ViewDtoResponse(app=test-app, uri=test-uri, views=2)
//        ViewDtoResponse(app=test-app2, uri=test-uri, views=2)
//        ViewDtoResponse(app=test-app2, uri=test-uri2, views=1)
//        ViewDtoResponse(app=test-app, uri=test-uri2, views=1)
        System.out.println(filter);
        responses.forEach(System.out::println);

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
        assertEquals(4, responses.size());
        assertEquals(1, responses.get(0).getViews(), "Error: get(0).getViews() not equal to 1 if ASC sort.");
        assertEquals(1, responses.get(1).getViews(), "Error: get(1).getViews() not equal to 1 if ASC sort.");
        assertEquals(2, responses.get(2).getViews(), "Error: get(2).getViews() not equal to 2 if ASC sort.");
        assertEquals(2, responses.get(3).getViews(), "Error: get(3).getViews() not equal to 2 if ASC sort.");
    }

    @Test
    public void find_shouldReturnWithCorrectViewsFields_whenAddedWithUniqueAsFalseOrNull() {
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

/////////////////////////////////////////////////
//        ViewDtoResponse(app=test-app, uri=test-uri, views=10)
//        ViewDtoResponse(app=test-app2, uri=test-uri, views=5)
//        ViewDtoResponse(app=test-app, uri=test-uri2, views=4)
//        ViewDtoResponse(app=test-app2, uri=test-uri2, views=3)
        System.out.println(filterUniqueNull);
        responsesByUniqueNull.forEach(System.out::println);

        assertNull(filterUniqueNull.getUnique(), "Error: filterUniqueNull.getUnique() is not null.");
        assertFalse(filterUniqueFalse.getUnique(), "Error: filterUniqueFalse.getUnique() is true.");
        assertEquals(responsesByUniqueNull, responsesByUniqueFalse, "Error: responsesByUniqueNull and responsesByUniqueFalse are not equal.");
        assertEquals(4, responsesByUniqueNull.size(), "Error: The size of responsesByUniqueNull is not equal to 2.");
        assertEquals(10, responsesByUniqueNull.get(0).getViews(), "Error: get(0).getViews() not equal to 10 if DESC sort.");
        assertEquals(5, responsesByUniqueNull.get(1).getViews(), "Error: get(1).getViews() not equal to 5 if DESC sort.");
        assertEquals(4, responsesByUniqueNull.get(2).getViews(), "Error: get(2).getViews() not equal to 4 if DESC sort.");
        assertEquals(3, responsesByUniqueNull.get(3).getViews(), "Error: get(3).getViews() not equal to 3 if DESC sort.");
    }

    @Test
    public void find_shouldReturnWithCorrectViewsFields_whenAddedWithUniqueAsFalseOrNull_2() {
        String app = "test-app";
        String uri = "test-uri";
        String ip = "test-ip";
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);

        ViewSearchFilter filterUniqueNull = ViewSearchFilter.builder().build().setNullFieldsToDefault();
        filterUniqueNull.setUnique(null);
        assert filterUniqueNull.getUnique() == null;

        ViewSearchFilter filterUniqueFalse = ViewSearchFilter.builder()
                .start(null)
                .end(null)
                .unique(null)
                .sortDirection(null)
                .pageSize(null)
                .pageNumber(null)
                .uris(null)
                .build().setNullFieldsToDefault();

        View View1 = new View()
                .setId(null)
                .setApp(app)
                .setUri(uri)
                .setIp(ip)
                .setTimestamp(dateTime);

        View View2 = new View()
                .setId(null)
                .setApp(app + "_2")
                .setUri(uri + "_2")
                .setIp(ip + "_2")
                .setTimestamp(dateTime.minusDays(1));

        viewMongoRepository.save(View1).block();
        viewMongoRepository.save(View2).block();

        Flux<ViewDtoResponse> actualResponses = viewMongoRepository.find(filterUniqueNull);
        List<ViewDtoResponse> responses = actualResponses.collectList().block();


        assertNotNull(responses);

//        responses.forEach(System.out::println);

        assertEquals(2, responses.size());
    }
}
