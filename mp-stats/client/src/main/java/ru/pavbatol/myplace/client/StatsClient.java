package ru.pavbatol.myplace.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import ru.pavbatol.myplace.dto.view.ViewDtoResponse;
import ru.pavbatol.myplace.dto.view.ViewSearchFilter;

import java.util.List;

@Slf4j
public class StatsClient {
    public static final String STATS = "stats";
    String serverUrl;
    private final WebClient webClient;

    public StatsClient(String serverUrl) {
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
                .queryParam("start", filter.getStart() == null ? "" : filter.getStart())
                .queryParam("end", filter.getEnd() == null ? "" : filter.getEnd())
                .queryParam("uris", filter.getUris() == null ? "" :  String.join(",", filter.getUris()))
                .queryParam("unique", filter.getUnique() == null ? "" : filter.getUnique());

        String uriStr = uriBuilder.toUriString();
        log.debug("Sending a request to base url: {}, to path: {}", serverUrl, uriStr);

//        WebClient.ResponseSpec responseSpec = webClient.get()
//                .uri(uriStr)
//                .retrieve();
//        return responseSpec.bodyToFlux(ViewDtoResponse.class);

        Flux<ViewDtoResponse> responseFlux = webClient.get()
                .uri(uriStr)
                .exchangeToFlux(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToFlux(ViewDtoResponse.class);
                    } else {
                        String errStr = "Request execution error: " + response.statusCode();
                        log.warn(errStr);
                        throw new RuntimeException(errStr);
                    }
                });
        return responseFlux;
    }

    public List<ViewDtoResponse> getViewsAsList(ViewSearchFilter filter) {
        Flux<ViewDtoResponse> viewsAsFlux = getViewsAsFlux(filter);
        return viewsAsFlux.collectList().block();
    }
}
