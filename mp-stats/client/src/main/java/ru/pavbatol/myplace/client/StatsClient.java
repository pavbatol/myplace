package ru.pavbatol.myplace.client;

import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StatsClient<T, R, F, R1> {
    Mono<R> add(@NonNull T t);

    R addByBlocking(@NonNull T t);

    Flux<R1> find(@NonNull F f);

    List<R1> findByBlocking(@NonNull F f);
}
