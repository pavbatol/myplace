package ru.pavbatol.myplace.gateway.profile.profile.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.shared.dto.pagination.PageDto;
import ru.pavbatol.myplace.shared.dto.profile.profile.*;
import ru.pavbatol.myplace.shared.enums.profile.profile.ProfileStatus;

import java.util.function.Consumer;
import java.util.function.Function;

import static ru.pavbatol.myplace.shared.constant.HttpHeaders.*;

@Slf4j
@Component
public class ProfileClientImpl implements ProfileClient {
    private static final String ADMIN_CONTEXT = "/admin/profiles";
    private static final String USER_CONTEXT = "/user/profiles";
    private static final String PUBLIC_CONTEXT = "/profiles";
    private final WebClient mutatedWebClient;

    public ProfileClientImpl(@Value("${app.mp.profile.url}") String serverUrl,
                             @Autowired Function<String, WebClient> webClientFactory) {
        this.mutatedWebClient = webClientFactory.apply(serverUrl);
    }

    @Override
    public Mono<ResponseEntity<ProfileDtoUpdateStatusResponse>> adminUpdateStatusByUserId(ProfileStatus profileStatus, HttpHeaders headers) {
        return mutatedWebClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path(ADMIN_CONTEXT)
                        .path("/status")
                        .queryParam("status", profileStatus.name())
                        .build())
                .headers(getDefaultHeadersConsumer(headers))
                .retrieve()
                .toEntity(ProfileDtoUpdateStatusResponse.class);
    }

    @Override
    public Mono<ResponseEntity<PageDto<ProfileDto>>> adminGetAll(int page, int size, HttpHeaders headers) {
        return mutatedWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ADMIN_CONTEXT)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .headers(getDefaultHeadersConsumer(headers))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<PageDto<ProfileDto>>() {
                });
    }

    @Override
    public Mono<ResponseEntity<ProfileDto>> adminGetById(Long profileId, HttpHeaders headers) {
        return mutatedWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ADMIN_CONTEXT)
                        .path("/" + profileId)
                        .build())
                .headers(getDefaultHeadersConsumer(headers))
                .retrieve()
                .toEntity(ProfileDto.class);
    }

    @Override
    public Mono<ResponseEntity<ProfileDto>> adminGetByUserId(HttpHeaders headers) {
        return mutatedWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ADMIN_CONTEXT)
                        .path("/user")
                        .build())
                .headers(getDefaultHeadersConsumer(headers))
                .retrieve()
                .toEntity(ProfileDto.class);
    }

    @Override
    public Mono<ResponseEntity<Void>> delete(Long profileId, HttpHeaders headers) {
        return mutatedWebClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path(USER_CONTEXT)
                        .path("/" + profileId)
                        .build())
                .headers(getDefaultHeadersConsumer(headers))
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public Mono<ResponseEntity<ProfileDto>> update(Long profileId, ProfileDtoUpdate dto, HttpHeaders headers) {
        return mutatedWebClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path(USER_CONTEXT)
                        .path("/" + profileId)
                        .build())
                .headers(getDefaultHeadersConsumer(headers))
                .bodyValue(dto)
                .retrieve()
                .toEntity(ProfileDto.class);
    }

    @Override
    public Mono<ResponseEntity<ProfileDto>> privateGetById(Long profileId, HttpHeaders headers) {
        return mutatedWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(USER_CONTEXT)
                        .path("/" + profileId)
                        .build())
                .headers(getDefaultHeadersConsumer(headers))
                .retrieve()
                .toEntity(ProfileDto.class);
    }

    @Override
    public Mono<ResponseEntity<ProfileDto>> privateGetByUserId(HttpHeaders headers) {
        return mutatedWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(USER_CONTEXT)
                        .build())
                .headers(getDefaultHeadersConsumer(headers))
                .retrieve()
                .toEntity(ProfileDto.class);
    }

    @Override
    public Mono<ResponseEntity<Boolean>> checkEmail(String email) {
        return mutatedWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PUBLIC_CONTEXT)
                        .path("/check-email")
                        .queryParam("email", email)
                        .build())
                .headers(headers -> {
                    headers.set(Accept, "application/json");
                })
                .retrieve()
                .toEntity(Boolean.class);
    }

    private static Consumer<HttpHeaders> getDefaultHeadersConsumer(HttpHeaders headers) {
        return hds -> {
            String userId = headers.getFirst(X_USER_ID);
            String userUuid = headers.getFirst(X_USER_UUID);

            if (userId != null) {
                hds.add(X_USER_ID, userId);
            }
            if (userUuid != null) {
                hds.add(X_USER_UUID, userUuid);
            }
        };
    }
}
