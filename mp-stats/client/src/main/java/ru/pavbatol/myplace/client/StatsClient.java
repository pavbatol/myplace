package ru.pavbatol.myplace.client;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.view.ViewDtoAddRequest;
import ru.pavbatol.myplace.dto.view.ViewDtoAddResponse;
import ru.pavbatol.myplace.dto.view.ViewDtoResponse;
import ru.pavbatol.myplace.dto.view.ViewSearchFilter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class StatsClient {
    public static final String STATS = "stats";
    public static final String DATE_TIME_T_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String VIEWS = "views";
    String serverUrl;
    private final WebClient webClient;
    private final DateTimeFormatter formatter;

    public StatsClient(String serverUrl) {
        this.formatter = DateTimeFormatter.ofPattern(DATE_TIME_T_PATTERN);
        this.serverUrl = serverUrl;
        this.webClient = WebClient.builder()
                .baseUrl(serverUrl)
                .build();
    }

    public Mono<ViewDtoAddResponse> add(@NonNull ViewDtoAddRequest dto) {
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment(STATS)
                        .pathSegment(VIEWS)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(dto), ViewDtoAddRequest.class)
                .retrieve()
                .onStatus(HttpStatus::isError, response -> response.bodyToMono(String.class)
                        .flatMap(strBody -> Mono.error(new RuntimeException(
                                "Request execution error: " + response.statusCode(), new Throwable(strBody)))))
                .bodyToMono(ViewDtoAddResponse.class);
    }

    public ViewDtoAddResponse addByBlocking(@NonNull ViewDtoAddRequest dto) {
        return add(dto).block();
    }

    public Flux<ViewDtoResponse> getViews(@NonNull ViewSearchFilter filter) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .pathSegment(STATS)
                .pathSegment(VIEWS)
                .queryParam("start", filter.getStart() == null ? "" : filter.getStart().format(formatter))
                .queryParam("end", filter.getEnd() == null ? "" : filter.getEnd().format(formatter))
                .queryParam("uris", filter.getUris() == null ? "" : String.join(",", filter.getUris()))
                .queryParam("unique", filter.getUnique() == null ? "" : filter.getUnique());

        String uriStr = uriBuilder.toUriString();
        log.debug("Sending a request to base url: {}, to path: {}", serverUrl, uriStr);

        WebClient.ResponseSpec responseSpec = webClient.get()
                .uri(uriStr)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve();

        return responseSpec
                .onStatus(HttpStatus::isError, response -> {
                    String errStr = "Request execution error: " + response.statusCode();
                    log.warn(errStr);
                    return response.bodyToMono(String.class)
                            .flatMap(body -> Mono.error(new RuntimeException(errStr, new Throwable(body))));
                })
                .bodyToFlux(ViewDtoResponse.class);
    }

    public List<ViewDtoResponse> getViewsByBlocking(ViewSearchFilter filter) {
        Flux<ViewDtoResponse> viewsAsFlux = getViews(filter);
        return viewsAsFlux.collectList().block();
    }
}
