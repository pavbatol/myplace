package ru.pavbatol.myplace.stats.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
