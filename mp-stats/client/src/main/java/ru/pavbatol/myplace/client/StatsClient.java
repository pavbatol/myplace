package ru.pavbatol.myplace.client;

import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StatsClient<T, R, F, R2> {
    Mono<R> add(@NonNull T t);

    R addByBlocking(@NonNull T t);

    Flux<R2> find(@NonNull F f);

    List<R2> findByBlocking(@NonNull F f);
}
