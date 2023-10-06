package ru.pavbatol.myplace.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pavbatol.myplace.user.model.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    boolean existsByLogin(String login);
}
