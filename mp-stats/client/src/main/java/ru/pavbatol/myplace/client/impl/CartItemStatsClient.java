package ru.pavbatol.myplace.client.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.client.AbstractStatsClient;
import ru.pavbatol.myplace.dto.cart.CartItemDtoAddRequest;
import ru.pavbatol.myplace.dto.cart.CartItemDtoAddResponse;
import ru.pavbatol.myplace.dto.cart.CartItemDtoResponse;
import ru.pavbatol.myplace.dto.cart.CartItemSearchFilter;

import java.util.stream.Collectors;

@Slf4j
public class CartItemStatsClient
        extends AbstractStatsClient<CartItemDtoAddRequest, CartItemDtoAddResponse, CartItemSearchFilter, CartItemDtoResponse> {

    public static final String CARTITEMS = "cartitems";

    public CartItemStatsClient(String serverUrl) {
        super(serverUrl);
    }

    @Override
    public Mono<CartItemDtoAddResponse> add(@NonNull CartItemDtoAddRequest dto) {
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment(STATS)
                        .pathSegment(CARTITEMS)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(dto), CartItemDtoAddRequest.class)
                .retrieve()
                .onStatus(HttpStatus::isError, response -> response.bodyToMono(String.class)
                        .flatMap(strBody -> Mono.error(new RuntimeException(
                                "Request execution error: " + response.statusCode(), new Throwable(strBody)))))
                .bodyToMono(CartItemDtoAddResponse.class);
    }

    @Override
    public Flux<CartItemDtoResponse> find(@NonNull CartItemSearchFilter filter) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .pathSegment(STATS)
                .pathSegment(CARTITEMS)
                .queryParam("start", filter.getStart() == null ? "" : filter.getStart().format(formatter))
                .queryParam("end", filter.getEnd() == null ? "" : filter.getEnd().format(formatter))
                .queryParam("itemIds", filter.getItemIds() == null ? "" : filter.getItemIds().stream()
                        .map(String::valueOf).collect(Collectors.joining(",")))
                .queryParam("unique", filter.getUnique() == null ? "" : filter.getUnique())
                .queryParam("sortDirection", filter.getSortDirection() == null ? "" : filter.getSortDirection());

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
                .bodyToFlux(CartItemDtoResponse.class);
    }
}
