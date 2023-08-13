package ru.pavbatol.myplace.server.view.repository;

import reactor.core.publisher.Flux;
import ru.pavbatol.myplace.dto.view.ViewDtoResponse;
import ru.pavbatol.myplace.dto.view.ViewSearchFilter;

public interface CustomViewMongoRepository {
    Flux<ViewDtoResponse> find(ViewSearchFilter filter);
}
