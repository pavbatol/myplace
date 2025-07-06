package ru.pavbatol.myplace.gateway.profile.profile.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.shared.dto.api.ApiError;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDtoUpdateStatusResponse;
import ru.pavbatol.myplace.shared.enums.profile.profile.ProfileStatus;
import ru.pavbatol.myplace.shared.exception.TargetServiceErrorException;
import ru.pavbatol.myplace.shared.exception.TargetServiceHandledErrorException;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileClientImpl implements ProfileClient {
    private final static String ADMIN_CONTEXT = "/admin/profiles";
    @Value("${app.mp.profile.url}")
    private String serverUrl;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<ResponseEntity<ProfileDtoUpdateStatusResponse>> adminUpdateStatusByUserId(ProfileStatus profileStatus, HttpHeaders headers) {
        String url = UriComponentsBuilder.fromUri(URI.create(serverUrl))
                .path(ADMIN_CONTEXT)
                .path("/status")
                .queryParam("status", profileStatus.name())
                .build(false)
                .toUriString();

        return webClient.patch()
                .uri(URI.create(url))
                .headers(hs -> hs.addAll(headers))
                .retrieve()
                .onStatus(HttpStatus::isError, this::handleTargetServiceError)
                .toEntity(ProfileDtoUpdateStatusResponse.class)
                .doOnError(TargetServiceErrorException.class, ex -> {
                    String handledMark = (ex instanceof TargetServiceHandledErrorException) ? "handled" : "not handled";
                    log.error("Target Service Error ({}): status={}, message={}", handledMark, ex.getStatus(), ex.getMessage());
                });
    }

    private Mono<? extends Throwable> handleTargetServiceError(ClientResponse response) {
        return response.bodyToMono(String.class)
                .defaultIfEmpty("No error details provided")
                .flatMap(errorBody -> {
                    try {
                        return Mono.error(new TargetServiceHandledErrorException(
                                objectMapper.readValue(errorBody, ApiError.class),
                                response.statusCode()
                        ));
                    } catch (Exception e) {
                        return Mono.error(new TargetServiceErrorException(
                                ApiError.message(errorBody, response.statusCode().toString()),
                                response.statusCode()
                        ));
                    }
                });
    }
}
