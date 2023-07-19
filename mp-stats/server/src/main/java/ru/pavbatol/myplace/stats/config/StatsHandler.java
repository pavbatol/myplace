package ru.pavbatol.myplace.stats.config;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class StatsHandler {
    public Mono<ServerResponse> getId(ServerRequest serverRequest) {
        Mono<Long> longMono = Mono.just(
                Long.parseLong(serverRequest.pathVariable("id")));
        return longMono.flatMap(aLong ->  ServerResponse.ok()
                        .body(fromValue(aLong))
                .switchIfEmpty(ServerResponse.notFound().build()));
    }

    public Mono<ServerResponse> getByName(ServerRequest serverRequest) {
        String name = serverRequest.queryParam("name").orElse(null);
        assert name != null;
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(name));
    }
}
