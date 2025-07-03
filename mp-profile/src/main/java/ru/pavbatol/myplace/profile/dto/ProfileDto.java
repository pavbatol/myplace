package ru.pavbatol.myplace.profile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import ru.pavbatol.myplace.profile.model.Gender;
import ru.pavbatol.myplace.profile.model.ProfileStatus;
import ru.pavbatol.myplace.shared.dto.profile.geo.house.HouseDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
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
