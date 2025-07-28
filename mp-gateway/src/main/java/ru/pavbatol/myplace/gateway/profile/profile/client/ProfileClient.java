package ru.pavbatol.myplace.gateway.profile.profile.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.shared.dto.pagination.PageDto;
import ru.pavbatol.myplace.shared.dto.profile.profile.*;
import ru.pavbatol.myplace.shared.enums.profile.profile.ProfileStatus;

public interface ProfileClient {
    Mono<ResponseEntity<ProfileDtoUpdateStatusResponse>> adminUpdateStatusByUserId(ProfileStatus profileStatus, HttpHeaders headers);

    Mono<ResponseEntity<PageDto<ProfileDto>>> adminGetAll(int page, int size, HttpHeaders headers);

    Mono<ResponseEntity<ProfileDto>> adminGetById(Long profileId, HttpHeaders headers);

    Mono<ResponseEntity<ProfileDto>> adminGetByUserId(HttpHeaders headers);

    Mono<ResponseEntity<Void>> delete(Long profileId, HttpHeaders headers);

    Mono<ResponseEntity<ProfileDto>> update(Long profileId, ProfileDtoUpdate dto, HttpHeaders headers);

    Mono<ResponseEntity<ProfileDto>> privateGetById(Long profileId, HttpHeaders headers);

    Mono<ResponseEntity<ProfileDto>> privateGetByUserId(HttpHeaders headers);
}
