package ru.pavbatol.myplace.profile.service;

import org.springframework.data.domain.Slice;
import ru.pavbatol.myplace.profile.dto.*;
import ru.pavbatol.myplace.profile.model.ProfileStatus;

import java.util.UUID;

public interface ProfileService {
    ProfileDtoCreateResponse create(UUID userUuid, ProfileDtoCreateRequest createRequest);

    boolean checkEmail(String email);

    ProfileDtoUpdateStatusResponse adminUpdateStatusByUserId(Long userId, UUID userUuid, ProfileStatus status);

    ProfileDto update(Long userId, UUID userUuid, Long profileId, ProfileDtoUpdate dto);

    void delete(Long profileId);

    ProfileDto privateGetById(Long userId, UUID userUuid, Long profileId);

    ProfileDto privateGetByUserId(Long userId, UUID userUuid);

    ProfileDto adminGetById(Long userId, UUID userUuid, Long profileId);

    ProfileDto adminGetByUserId(Long userId, UUID userUuid);

    Slice<ProfileDto> adminGetAll(int page, int size);
}
