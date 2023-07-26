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

@Slf4j
public class ViewStatsClient
        extends AbstractStatsClient<ViewDtoAddRequest, ViewDtoAddResponse, ViewSearchFilter, ViewDtoResponse> {

    public static final String VIEWS = "views";

    public ViewStatsClient(String serverUrl) {
        super(serverUrl);
    }

    @Override
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

    @Override
    public Flux<ViewDtoResponse> find(@NonNull ViewSearchFilter filter) {

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
}
