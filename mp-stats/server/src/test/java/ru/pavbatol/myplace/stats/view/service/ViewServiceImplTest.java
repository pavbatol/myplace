package ru.pavbatol.myplace.stats.view.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.pavbatol.myplace.dto.SortDirection;
import ru.pavbatol.myplace.dto.view.ViewDtoAddRequest;
import ru.pavbatol.myplace.dto.view.ViewDtoAddResponse;
import ru.pavbatol.myplace.dto.view.ViewDtoResponse;
import ru.pavbatol.myplace.dto.view.ViewSearchFilter;
import ru.pavbatol.myplace.stats.view.mapper.ViewMapper;
import ru.pavbatol.myplace.stats.view.model.View;
import ru.pavbatol.myplace.stats.view.repository.ViewMongoRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewServiceImplTest {

    @Mock
    private ViewMongoRepository repository;
    private final ViewMapper mapper = Mappers.getMapper(ViewMapper.class);
    private ViewServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ViewServiceImpl(repository, mapper);
    }

    @Test
    void add_shouldCorrectMappingAndPresetFieldValuesAndInvokeRepoAndAdded() {
        String app = "test-app";
        String uri = "test-uri";
        String ip = "test-ip";
        LocalDateTime dateTime = LocalDateTime.now();

        ViewDtoAddRequest addRequest = new ViewDtoAddRequest(app, uri, ip, dateTime);

        View addedView = new View()
                .setId(null)
                .setApp(app)
                .setUri(uri)
                .setIp(ip)
                .setTimestamp(dateTime);

        when(repository.save(any())).thenReturn(Mono.just(addedView));

        Mono<ViewDtoAddResponse> result = service.add(addRequest);

        assertThat(mapper.toEntity(addRequest))
                .extracting(View::getId, View::getApp, View::getUri, View::getIp, View::getTimestamp)
                .containsExactly(
                        addedView.getId(),
                        addedView.getApp(),
                        addedView.getUri(),
                        addedView.getIp(),
                        addedView.getTimestamp()
                );

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getApp().equals(app)
                                && response.getUri().equals(uri)
                                && response.getIp().equals(ip)
                                && response.getTimestamp().isEqual(dateTime)
                )
                .verifyComplete();

        verify(repository, times(1)).save(any());
    }

    @Test
    void add_shouldDateTimeNotNullWenItReceivedAsNull() {

        ViewDtoAddRequest addRequest = new ViewDtoAddRequest();

        View addedView = new View()
                .setTimestamp(null);

        when(repository.save(any())).thenReturn(Mono.just(addedView));

        service.add(addRequest);

        verify(repository, times(1)).save(argThat(view -> view.getTimestamp() != null));
    }

    @Test
    void find_shouldFilterFieldsIsCheckedBeforeSendingRequestToDB() {
        LocalDateTime dateTimePast = LocalDateTime.now().minusSeconds(1);

        ViewSearchFilter filter = ViewSearchFilter.builder()
                .start(null)
                .end(null)
                .uris(null)
                .unique(null)
                .sortDirection(null)
                .pageNumber(null)
                .pageSize(null)
                .build();

        ViewDtoResponse viewDtoResponse1 = ViewDtoResponse.builder()
                .build();

        when(repository.find(any())).thenReturn(Flux.just(viewDtoResponse1, viewDtoResponse1));

        Flux<ViewDtoResponse> result = service.find(filter);

        StepVerifier.create(result).expectNextCount(2);

        verify(repository, times(1)).find(argThat(searchFilter ->
                searchFilter.getStart().isBefore(dateTimePast)
                        && searchFilter.getEnd().isAfter(dateTimePast)
                        && searchFilter.getUris().equals(List.of())
                        && !searchFilter.getUnique()
                        && searchFilter.getSortDirection() == SortDirection.DESC
                        && searchFilter.getPageNumber() == 1
                        && searchFilter.getPageSize() == 10
        ));
    }
}