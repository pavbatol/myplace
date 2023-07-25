package ru.pavbatol.myplace.stats.view.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.view.ViewDtoAddRequest;
import ru.pavbatol.myplace.dto.view.ViewDtoAddResponse;
import ru.pavbatol.myplace.dto.view.ViewDtoResponse;
import ru.pavbatol.myplace.dto.view.ViewSearchFilter;
import ru.pavbatol.myplace.stats.view.service.ViewService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
public class ViewController {

    private final ViewService viewService;

//    @RequestMapping("/test")
//    public String test() {
//        return "This is a test";
//    }

    @PostMapping("/views")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ViewDtoAddResponse> add(@Valid @RequestBody ViewDtoAddRequest dto) {
        log.debug("POST (add) with dto={},", dto);
        return viewService.add(dto);
    }

    @GetMapping("/views")
    public Flux<ViewDtoResponse> getViews_test(ViewSearchFilter filter) {
        log.debug("GET (get) with filter={}", filter);
        System.out.println("filter.getStart() = " + filter.getStart());
        System.out.println("filter.getEnd() = " + filter.getEnd());
        System.out.println("filter.getUris() = " + filter.getUris());
        System.out.println("filter.getUnique() = " + filter.getUnique());
        return viewService.find(filter);
    }


    /*
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
        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
        DataBuffer buf = dataBufferFactory.wrap("Hello from exchange".getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Flux.just(buf));
    }

    @RequestMapping("/error")
    public Mono<?> error() {
        return Mono.error(new IllegalArgumentException("My custom error message"))
                .onErrorResume(
                        WebClientResponseException.class,
                        ex -> Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .body(ex.getResponseBodyAsString())
                ));
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

*/

}
