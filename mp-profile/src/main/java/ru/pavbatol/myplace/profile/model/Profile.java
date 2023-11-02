package ru.pavbatol.myplace.profile.model;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.pavbatol.myplace.geo.house.model.House;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    Long id;

    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(name = "email", nullable = false)
    String email;

    @Column(name = "trusted_email")
    String trustedEmail;

    @Column(name = "mobile_number")
    String mobileNumber;

    @Column(name = "trusted_mobile_number")
    String trustedMobileNumber;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "second_name")
    String secondName;

    @Column(name = "birthday")
    LocalDateTime birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    Gender gender;

    @ManyToOne
    @JoinColumn(name = "house_id")
    House house;

    @Column(name = "apartment")
    String apartment;

    @Column(name = "avatar")
    byte[] avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    ProfileStatus status;

    @Column(name = "created_on", nullable = false)
    LocalDateTime createdOn;
}
