package ru.pavbatol.myplace.gateway.profile.profile.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.shared.dto.pagination.PageDto;
import ru.pavbatol.myplace.shared.dto.profile.profile.*;
import ru.pavbatol.myplace.shared.enums.profile.profile.ProfileStatus;

public interface ProfileService {
    Mono<ApiResponse<ProfileDtoUpdateStatusResponse>> adminUpdateStatusByUserId(ProfileStatus profileStatus, HttpHeaders headers);

    Mono<ApiResponse<PageDto<ProfileDto>>> adminGetAll(int page, int size, HttpHeaders headers);

    Mono<ApiResponse<ProfileDto>> adminGetById(Long profileId, HttpHeaders headers);

    Mono<ApiResponse<ProfileDto>> adminGetByUserId(HttpHeaders headers);

    Mono<ResponseEntity<Void>> delete(Long profileId, HttpHeaders headers);

    Mono<ApiResponse<ProfileDto>> update(Long profileId, ProfileDtoUpdate dto, HttpHeaders headers);

    Mono<ApiResponse<ProfileDto>> privateGetById(Long profileId, HttpHeaders headers);

    Mono<ApiResponse<ProfileDto>> privateGetByUserId(HttpHeaders headers);

    Mono<ApiResponse<Boolean>> checkEmail(String email);
}
