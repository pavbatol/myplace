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
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDto;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDtoCreateRequest;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDtoCreateResponse;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDtoUpdateStatusResponse;
import ru.pavbatol.myplace.shared.enums.profile.profile.ProfileStatus;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Component
public class ProfileClientImpl implements ProfileClient {
    private final static String ADMIN_CONTEXT = "/admin/profiles";
    private final static String USER_CONTEXT = "/user/profiles";
    private static final String X_USER_ID = "X-User-Id";
    private static final String X_USER_UUID = "X-User-Uuid";
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
                .headers(getHeadersConsumer(headers))
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
                .headers(getHeadersConsumer(headers))
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
                .headers(getHeadersConsumer(headers))
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
                .headers(getHeadersConsumer(headers))
                .retrieve()
                .toEntity(ProfileDto.class);
    }

    @Override
    public Mono<ResponseEntity<ProfileDtoCreateResponse>> create(ProfileDtoCreateRequest dto, HttpHeaders headers) {
        return mutatedWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(USER_CONTEXT)
                        .build())
                .headers(getHeadersConsumer(headers))
                .bodyValue(dto)
                .retrieve()
                .toEntity(ProfileDtoCreateResponse.class);
    }

    private static Consumer<HttpHeaders> getHeadersConsumer(HttpHeaders headers) {
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
