package ru.pavbatol.myplace.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pavbatol.myplace.user.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    boolean existsByLogin(String login);

    Optional<User> findByUuid(UUID uuid);

    @Query("select u.id from User u where u.uuid = :uuid")
    Optional<Long> getIdByUuid(UUID uuid);
}
