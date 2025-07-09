package ru.pavbatol.myplace.shared.dto.profile.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.pavbatol.myplace.shared.dto.profile.geo.house.HouseDto;
import ru.pavbatol.myplace.shared.enums.profile.profile.Gender;
import ru.pavbatol.myplace.shared.enums.profile.profile.ProfileStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
@Jacksonized
public class ProfileDto {
    Long id;

    Long userId;

    UUID userUuid;

    String email;

    String trustedEmail;

    String mobileNumber;

    String trustedMobileNumber;

    String firstName;

    String secondName;

    LocalDateTime birthday;

    Gender gender;

    HouseDto house;

    String apartment;

    @JsonProperty("avatar")
    String encodedAvatar;

    ProfileStatus status;

    LocalDateTime changedStatusOn;

    LocalDateTime createdOn;
}
