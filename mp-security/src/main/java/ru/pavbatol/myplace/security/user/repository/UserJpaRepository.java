package ru.pavbatol.myplace.security.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pavbatol.myplace.security.user.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    boolean existsByLogin(String login);

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByLogin(String login);

    @Query("select u.id from User u where u.uuid = :uuid")
    Optional<Long> getIdByUuid(UUID uuid);
}
