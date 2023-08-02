package ru.pavbatol.myplace.client.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.client.AbstractStatsClient;
import ru.pavbatol.myplace.dto.view.ViewDtoAddRequest;
import ru.pavbatol.myplace.dto.view.ViewDtoAddResponse;
import ru.pavbatol.myplace.dto.view.ViewDtoResponse;
import ru.pavbatol.myplace.dto.view.ViewSearchFilter;

@Slf4j
public class ViewStatsClient
        extends AbstractStatsClient<ViewDtoAddRequest, ViewDtoAddResponse, ViewSearchFilter, ViewDtoResponse> {

    public static final String VIEWS = "views";

    public ViewStatsClient(String serverUrl) {
        super(serverUrl);
    }

    @Override
    public Mono<ViewDtoAddResponse> add(@NonNull ViewDtoAddRequest dto) {
        String path = UriComponentsBuilder.newInstance()
                .pathSegment(STATS)
                .pathSegment(VIEWS)
                .build().toUriString();

        return post(path, dto, ViewDtoAddResponse.class);
    }

    @Override
    public Flux<ViewDtoResponse> find(@NonNull ViewSearchFilter filter) {
        String path = UriComponentsBuilder.newInstance()
                .pathSegment(STATS)
                .pathSegment(VIEWS)
                .query(filter.toQuery(formatter))
                .build().toUriString();

        return get(path, ViewDtoResponse.class);
    }
}
