package ru.pavbatol.myplace.profile.dto;

import lombok.Value;
import ru.pavbatol.myplace.geo.house.dto.HouseDto;
import ru.pavbatol.myplace.profile.model.Gender;
import ru.pavbatol.myplace.profile.model.ProfileStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class ProfileDto {
    Long id;

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

    byte[] avatar;

    ProfileStatus status;

    LocalDateTime changedStatusOn;

    LocalDateTime createdOn;
}
