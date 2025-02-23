package ru.pavbatol.myplace.profile.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pavbatol.myplace.profile.model.Profile;
import ru.pavbatol.myplace.profile.model.ProfileStatus;

import java.util.Optional;

@Repository
public interface ProfileJpaRepository extends JpaRepository<Profile, Long> {
    boolean existsByEmail(String email);

    @EntityGraph(value = "profile.house")
    Optional<Profile> findByUserId(Long userId);

    @NonNull
    @EntityGraph(value = "profile.house")
    @Override
    Optional<Profile> findById(@NonNull Long id);

    @EntityGraph(value = "profile.house")
    @Query("SELECT p FROM Profile p WHERE p.userId = :userId AND p.status <> :status")
    Optional<Profile> findByUserIdAndNotStatus(@Param("userId") Long userId, @Param("status") ProfileStatus status);

    @EntityGraph(value = "profile.house")
    @Query("SELECT p FROM Profile p WHERE p.id = :id AND p.status <> :status")
    Optional<Profile> findByIdAndNotStatus(@Param("id") Long userId, @Param("status") ProfileStatus status);
}
