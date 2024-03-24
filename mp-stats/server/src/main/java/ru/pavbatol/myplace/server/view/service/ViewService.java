package ru.pavbatol.myplace.server.view.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.view.ViewDtoAddRequest;
import ru.pavbatol.myplace.dto.view.ViewDtoAddResponse;
import ru.pavbatol.myplace.dto.view.ViewDtoResponse;
import ru.pavbatol.myplace.dto.view.ViewSearchFilter;

public interface ViewService {
    Mono<ViewDtoAddResponse> add(ViewDtoAddRequest dto);

    Flux<ViewDtoResponse> find(ViewSearchFilter filter);
}
