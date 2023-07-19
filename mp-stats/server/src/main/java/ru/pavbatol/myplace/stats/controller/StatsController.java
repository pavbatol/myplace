package ru.pavbatol.myplace.stats.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/demo")
public class StatsController {
    private final DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();


    @RequestMapping("/test")
    public String test() {
        return "This is a test";
    }

    @RequestMapping("/hello")
    public Mono<String> hello(@RequestParam String name) {
        return Mono.just("Hello, " + name + "!");
    }

    @RequestMapping("/exchange")
    public Mono<Void> exchange(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
        DataBuffer buf = dataBufferFactory.wrap("Hello from exchange".getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Flux.just(buf));
    }

    @RequestMapping("/error")
    public Mono<String> error() {
        return Mono.error(new IllegalArgumentException("My custom error message"));
    }

    @RequestMapping("/{id}")
    public Mono<ResponseEntity<Long>> getId(@PathVariable long id) {
        return Mono.just(id)
                .map(aLong -> ResponseEntity.status(HttpStatus.CREATED).body(aLong))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @RequestMapping("str/{str}")
    public Mono<ResponseEntity<String>> getStr(@PathVariable String str) {
        return Mono.just(str)
                .flatMap(s ->
                        Mono.just(ResponseEntity.status(HttpStatus.OK).body("bbb"))
                                .then(Mono.just(new ResponseEntity<String>("ccc", HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
