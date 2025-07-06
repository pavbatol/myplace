package ru.pavbatol.myplace.gateway.profile.profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.profile.profile.client.ProfileClient;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDtoUpdateStatusResponse;
import ru.pavbatol.myplace.shared.enums.profile.profile.ProfileStatus;

@Component
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileClient client;

    @Override
    public Mono<ApiResponse<ProfileDtoUpdateStatusResponse>> adminUpdateStatusByUserId(ProfileStatus profileStatus, HttpHeaders headers) {
        Mono<ResponseEntity<ProfileDtoUpdateStatusResponse>> response = client.adminUpdateStatusByUserId(profileStatus, headers);

        return response.
                map(responseEntity -> {
                            ProfileDtoUpdateStatusResponse body = responseEntity.getBody();
                            if (body == null) {
                                throw new IllegalStateException("Response body is null for status: " + responseEntity.getStatusCode());
                            }
                            return ApiResponse.success(
                                    body,
                                    responseEntity.getStatusCode());
                        }
                );
    }
}
