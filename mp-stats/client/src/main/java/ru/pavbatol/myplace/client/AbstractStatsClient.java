package ru.pavbatol.myplace.client;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.annotation.ExcludeJacocoGenerated;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@ExcludeJacocoGenerated
public abstract class AbstractStatsClient<T, R, F, R1> implements StatsClient<T, R, F, R1> {
    public static final String STATS = "stats";
    public static final String DATE_TIME_T_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    protected final String serverUrl;
    protected final WebClient webClient;
    protected final DateTimeFormatter formatter;

    public AbstractStatsClient(String serverUrl) {
        this.formatter = DateTimeFormatter.ofPattern(DATE_TIME_T_PATTERN);
        this.serverUrl = serverUrl;
        this.webClient = WebClient.builder()
                .baseUrl(serverUrl)
                .build();
    }

    @Override
    public R addByBlocking(@NonNull T t) {
        return add(t).block();
    }

    @Override
    public List<R1> findByBlocking(@NonNull F f) {
        Flux<R1> asFlux = find(f);
        return asFlux.collectList().block();
    }

    protected <B, V> Mono<V> post(String path, B body, Class<V> responseClass) {
        log.debug("Sending request to base url: {}, to path: {}, with body: {}", serverUrl, path, body);
        return webClient
                .post()
                .uri(path)
                .headers(headers -> headers.addAll(defaultHeaders()))
                .body(Mono.just(body), body.getClass())
                .retrieve()
                .onStatus(HttpStatus::isError, response -> response.bodyToMono(String.class)
                        .flatMap(strBody -> Mono.error(createRequestException(response.statusCode(), strBody))))
                .bodyToMono(responseClass);
    }

    protected <V> Flux<V> get(String path, Class<V> responseClass) {
        log.debug("Sending request to base url: {}, to path: {}", serverUrl, path);
        WebClient.ResponseSpec responseSpec = webClient.get()
                .uri(path)
                .headers(headers -> headers.addAll(defaultHeaders()))
                .retrieve();

        return responseSpec
                .onStatus(HttpStatus::isError, response -> response.bodyToMono(String.class)
                        .flatMap(strBody -> Mono.error(createRequestException(response.statusCode(), strBody))))
                .bodyToFlux(responseClass);
    }

    private Throwable createRequestException(HttpStatus status, String message) {
        return new RuntimeException("Request execution error: " + status, new Throwable(message));
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
