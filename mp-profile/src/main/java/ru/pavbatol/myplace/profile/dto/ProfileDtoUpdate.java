package ru.pavbatol.myplace.profile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import ru.pavbatol.myplace.profile.model.Gender;
import ru.pavbatol.myplace.shared.dto.profile.geo.house.HouseDto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
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

    @Past
    LocalDateTime birthday;

    Gender gender;

    HouseDto house;

    @Size(max = 10)
    String apartment;

    @JsonProperty("avatar")
    String encodedAvatar;

    @AssertTrue(message = "Both 'house' and 'apartment' must be either filled or null")
    public boolean isHouseAndApartmentValid() {
        return (house != null && apartment != null && !apartment.trim().isEmpty()) || (house == null && apartment == null);
    }
}
