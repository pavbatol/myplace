package ru.pavbatol.myplace.profile.service;

import ru.pavbatol.myplace.profile.dto.ProfileDtoCreateRequest;
import ru.pavbatol.myplace.profile.dto.ProfileDtoCreateResponse;
import ru.pavbatol.myplace.profile.dto.ProfileDtoUpdateStatus;
import ru.pavbatol.myplace.profile.model.ProfileStatus;

import java.util.UUID;

public interface ProfileService {
    ProfileDtoCreateResponse create(UUID userUuid, ProfileDtoCreateRequest createRequest);

    boolean checkEmail(String email);

    ProfileDtoUpdateStatus updateStatus(Long userId, UUID userUuid, ProfileStatus profileStatus);
}
