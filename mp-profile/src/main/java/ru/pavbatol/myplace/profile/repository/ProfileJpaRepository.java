package ru.pavbatol.myplace.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pavbatol.myplace.profile.model.Profile;

import java.util.Optional;

@Repository
public interface ProfileJpaRepository extends JpaRepository<Profile, Long> {
    boolean existsByEmail(String email);

    Optional<Profile> findByUserId(Long userId);
}
