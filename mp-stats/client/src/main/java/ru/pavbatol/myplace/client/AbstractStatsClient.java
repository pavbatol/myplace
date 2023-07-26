package ru.pavbatol.myplace.client;

import lombok.NonNull;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class AbstractStatsClient<T, R, F, R2> implements StatsClient<T, R, F, R2> {
    public static final String STATS = "stats";
    public static final String DATE_TIME_T_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    String serverUrl;
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
    public List<R2> findByBlocking(@NonNull F f) {
        Flux<R2> asFlux = find(f);
        return asFlux.collectList().block();
    }
}
