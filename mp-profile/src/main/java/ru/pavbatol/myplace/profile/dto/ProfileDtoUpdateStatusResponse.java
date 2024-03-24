package ru.pavbatol.myplace.profile.dto;

import lombok.Value;
import ru.pavbatol.myplace.profile.model.ProfileStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class ProfileDtoUpdateStatusResponse {
    Long id;
    UUID userUuid;
    String email;
    ProfileStatus status;
    LocalDateTime changedStatusOn;
}
