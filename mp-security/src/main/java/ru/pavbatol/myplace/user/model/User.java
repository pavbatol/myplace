package ru.pavbatol.myplace.user.model;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long id;

    @Column(name = "user_uuid", nullable = false, unique = true)
    UUID uuid;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "login", nullable = false, unique = true)
    String login;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "blocked", nullable = false)
    Boolean blocked;

    @Column(name = "deleted", nullable = false)
    Boolean deleted;

    @Column(name = "first_name", nullable = false)
    String firstName;

    @Column(name = "registered_on", nullable = false)
    LocalDateTime registeredOn;
}
