package ru.pavbatol.myplace.shared.dto.profile.profile;

import lombok.Value;
import ru.pavbatol.myplace.shared.enums.profile.profile.ProfileStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class ProfileDtoCreateResponse {
    Long id;
    UUID userUuid;
    String email;
    ProfileStatus status;
    LocalDateTime createdOn;
}
