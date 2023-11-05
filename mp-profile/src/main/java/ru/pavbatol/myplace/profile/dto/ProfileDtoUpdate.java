package ru.pavbatol.myplace.profile.dto;

import lombok.Value;
import ru.pavbatol.myplace.geo.house.dto.HouseDto;
import ru.pavbatol.myplace.profile.model.Gender;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Value
public class ProfileDtoUpdate {

    @Email
    String email;

    @Email
    String trustedEmail;

    @Size(min = 10, max = 12)
    String mobileNumber;

    @Size(min = 10, max = 12)
    String trustedMobileNumber;

    @Size(max = 100)
    String firstName;

    @Size(max = 100)
    String secondName;

    LocalDateTime birthday;

    Gender gender;

    HouseDto house;

    @Size(max = 10)
    String apartment;

    @Size(max = 3 * 1024 * 1024)
    byte[] avatar;
}
