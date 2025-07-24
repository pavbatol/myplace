package ru.pavbatol.myplace.gateway.profile.profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.profile.profile.client.ProfileClient;
import ru.pavbatol.myplace.shared.dto.pagination.PageDto;
import ru.pavbatol.myplace.shared.dto.profile.profile.*;
import ru.pavbatol.myplace.shared.enums.profile.profile.ProfileStatus;

@Component
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileClient client;

    @Override
    public Mono<ApiResponse<ProfileDtoUpdateStatusResponse>> adminUpdateStatusByUserId(ProfileStatus profileStatus, HttpHeaders headers) {
        Mono<ResponseEntity<ProfileDtoUpdateStatusResponse>> response = client.adminUpdateStatusByUserId(profileStatus, headers);

        return response.map(this::convertToApiResponse);
    }

    @Override
    public Mono<ApiResponse<PageDto<ProfileDto>>> adminGetAll(int page, int size, HttpHeaders headers) {
        Mono<ResponseEntity<PageDto<ProfileDto>>> response = client.adminGetAll(page, size, headers);

        return response.map(this::convertToApiResponse);
    }

    @Override
    public Mono<ApiResponse<ProfileDto>> adminGetById(Long profileId, HttpHeaders headers) {
        Mono<ResponseEntity<ProfileDto>> response = client.adminGetById(profileId, headers);

        return response.map(this::convertToApiResponse);
    }

    @Override
    public Mono<ApiResponse<ProfileDto>> adminGetByUserId(HttpHeaders headers) {
        Mono<ResponseEntity<ProfileDto>> response = client.adminGetByUserId(headers);

        return response.map(this::convertToApiResponse);
    }

    @Override
    public Mono<ResponseEntity<Void>> delete(Long profileId, HttpHeaders headers) {
        return client.delete(profileId, headers);
    }

    @Override
    public Mono<ApiResponse<ProfileDto>> update(Long profileId, ProfileDtoUpdate dto, HttpHeaders headers) {
        Mono<ResponseEntity<ProfileDto>> response = client.update(profileId, dto, headers);

        return response.map(this::convertToApiResponse);
    }

    private <T> ApiResponse<T> convertToApiResponse(ResponseEntity<T> responseEntity) {
        T body = responseEntity.getBody();
        if (body == null) {
            throw new IllegalStateException("Response body is null for status: " + responseEntity.getStatusCode());
        }

        return ApiResponse.success(body, responseEntity.getStatusCode());
    }
}
