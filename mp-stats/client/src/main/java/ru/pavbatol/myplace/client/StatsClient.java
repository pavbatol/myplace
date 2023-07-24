package ru.pavbatol.myplace.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.view.ViewDtoResponse;
import ru.pavbatol.myplace.dto.view.ViewSearchFilter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class StatsClient {
    public static final String STATS = "stats";
    public static final String DATE_TIME_T_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
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

    public Flux<ViewDtoResponse> getViewsAsFlux(ViewSearchFilter filter) {
        String path = "views-test";

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .pathSegment(STATS)
                .pathSegment(path)
                .queryParam("start", filter.getStart() == null ? "" : filter.getStart().format(formatter))
                .queryParam("end", filter.getEnd() == null ? "" : filter.getEnd().format(formatter))
                .queryParam("uris", filter.getUris() == null ? "" : String.join(",", filter.getUris()))
                .queryParam("unique", filter.getUnique() == null ? "" : filter.getUnique());

        String uriStr = uriBuilder.toUriString();
        log.debug("Sending a request to base url: {}, to path: {}", serverUrl, uriStr);

        WebClient.ResponseSpec responseSpec = webClient.get()
                .uri(uriStr)
                .retrieve();

        return responseSpec
                .onStatus(HttpStatus::isError, response -> {
                    String errStr = "Request execution error: " + response.statusCode();
                    log.warn(errStr);
                    return response.bodyToMono(String.class)
                            .flatMap(body -> Mono.error(new RuntimeException(errStr, new Throwable(body))));
                })
                .bodyToFlux(ViewDtoResponse.class);

//        return webClient.get()
//                .uri(uriStr)
//                .exchangeToFlux(response -> {
//                    if (response.statusCode().is2xxSuccessful()) {
//                        return response.bodyToFlux(ViewDtoResponse.class);
//                    } else {
//                        String errStr = "Request execution error: " + response.statusCode();
//                        log.warn(errStr);
//                        throw new RuntimeException(errStr + ":" + response.bodyToMono(String.class).block());
//                    }
//                });
    }

    public List<ViewDtoResponse> getViewsAsList(ViewSearchFilter filter) {
        Flux<ViewDtoResponse> viewsAsFlux = getViewsAsFlux(filter);
        return viewsAsFlux.collectList().block();
    }
}
