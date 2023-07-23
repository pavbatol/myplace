package ru.pavbatol.myplace.stats.view.repository;

import reactor.core.publisher.Flux;
import ru.pavbatol.myplace.dto.view.ViewSearchFilter;
import ru.pavbatol.myplace.stats.view.model.View;

public interface CustomViewMongoRepository {
    Flux<View> find(ViewSearchFilter filter);
}
