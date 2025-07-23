package ru.pavbatol.myplace.gateway.profile.profile.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.shared.dto.pagination.PageDto;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDto;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDtoCreateRequest;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDtoCreateResponse;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDtoUpdateStatusResponse;
import ru.pavbatol.myplace.shared.enums.profile.profile.ProfileStatus;

public interface ProfileService {
    Mono<ApiResponse<ProfileDtoUpdateStatusResponse>> adminUpdateStatusByUserId(ProfileStatus profileStatus, HttpHeaders headers);

    Mono<ApiResponse<PageDto<ProfileDto>>> adminGetAll(int page, int size, HttpHeaders headers);

    Mono<ApiResponse<ProfileDto>> adminGetById(Long profileId, HttpHeaders headers);

    Mono<ApiResponse<ProfileDto>> adminGetByUserId(HttpHeaders headers);

    Mono<ApiResponse<ProfileDtoCreateResponse>> create(ProfileDtoCreateRequest dto, HttpHeaders headers);

    Mono<ResponseEntity<Void>> delete(Long profileId, HttpHeaders headers);
}
