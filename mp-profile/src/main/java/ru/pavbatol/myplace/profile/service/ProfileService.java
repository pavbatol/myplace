package ru.pavbatol.myplace.profile.service;

import ru.pavbatol.myplace.profile.model.ProfileStatus;
import ru.pavbatol.myplace.shared.dto.profile.profile.*;
import ru.pavbatol.myplace.shared.pagination.SimplePage;

import java.util.UUID;

public interface ProfileService {
    ProfileDtoCreateResponse create(UUID userUuid, ProfileDtoCreateRequest createRequest);

    boolean checkEmail(String email);

    ProfileDtoUpdateStatusResponse adminUpdateStatusByUserId(Long userId, UUID userUuid, ProfileStatus status);

    ProfileDto update(Long userId, UUID userUuid, Long profileId, ProfileDtoUpdate dto);

    void delete(Long profileId, Long userId);

    ProfileDto privateGetById(Long userId, UUID userUuid, Long profileId);

    ProfileDto privateGetByUserId(Long userId, UUID userUuid);

    ProfileDto adminGetById(Long userId, UUID userUuid, Long profileId);

    ProfileDto adminGetByUserId(Long userId, UUID userUuid);

    SimplePage<ProfileDto> adminGetAll(int page, int size);
}
