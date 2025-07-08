package ru.pavbatol.myplace.gateway.profile.profile.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDtoUpdateStatusResponse;
import ru.pavbatol.myplace.shared.enums.profile.profile.ProfileStatus;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Component
public class ProfileClientImpl implements ProfileClient {
    private final static String ADMIN_CONTEXT = "/admin/profiles";
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
                .headers(getHeadersConsumer(headers)
                )
                .retrieve()
                .toEntity(ProfileDtoUpdateStatusResponse.class);
    }

    private static Consumer<HttpHeaders> getHeadersConsumer(HttpHeaders headers) {
        return hds -> {
            hds.add(X_USER_ID, headers.getFirst(X_USER_ID));
            hds.add(X_USER_UUID, headers.getFirst(X_USER_UUID));
        };
    }
}
