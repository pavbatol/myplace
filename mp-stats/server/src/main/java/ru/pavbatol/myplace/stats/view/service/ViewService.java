package ru.pavbatol.myplace.stats.view.service;

import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.view.ViewDtoAddRequest;
import ru.pavbatol.myplace.dto.view.ViewDtoAddResponse;
import ru.pavbatol.myplace.dto.view.ViewDtoResponse;

import java.util.List;

public interface ViewService {
    Mono<ViewDtoAddResponse> add(ViewDtoAddRequest dto);

    List<ViewDtoResponse> find(String start, String end, List<String> uris, Boolean unique);
}
