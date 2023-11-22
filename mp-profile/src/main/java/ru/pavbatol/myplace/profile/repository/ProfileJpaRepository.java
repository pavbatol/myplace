package ru.pavbatol.myplace.profile.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pavbatol.myplace.profile.model.Profile;

import java.util.Optional;

@Repository
public interface ProfileJpaRepository extends JpaRepository<Profile, Long> {
    boolean existsByEmail(String email);

    @EntityGraph(value = "profile.house")
    Optional<Profile> findByUserId(Long userId);

    @NonNull
    @EntityGraph(value = "profile.house")
    Optional<Profile> findById(@NonNull Long id);
}
