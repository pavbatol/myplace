package ru.pavbatol.myplace.profile.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.app.Util.Checker;
import ru.pavbatol.myplace.profile.dto.ProfileDtoCreateRequest;
import ru.pavbatol.myplace.profile.dto.ProfileDtoCreateResponse;
import ru.pavbatol.myplace.profile.dto.ProfileDtoUpdateStatus;
import ru.pavbatol.myplace.profile.mapper.ProfileMapper;
import ru.pavbatol.myplace.profile.model.Profile;
import ru.pavbatol.myplace.profile.model.ProfileStatus;
import ru.pavbatol.myplace.profile.repository.ProfileJpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private static final String ENTITY_SIMPLE_NAME = Profile.class.getSimpleName();
    private final ProfileJpaRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Override
    public ProfileDtoCreateResponse create(UUID userUuid, ProfileDtoCreateRequest createRequest) {
        LocalDateTime now = LocalDateTime.now();
        Profile profile = profileMapper.toEntity(createRequest)
                .setStatus(ProfileStatus.ACTIVE)
                .setCreatedOn(now)
                .setChangedStatusOn(now);
        Profile saved = profileRepository.save(profile);
        log.debug("{} created: {}", ENTITY_SIMPLE_NAME, saved);

        return profileMapper.toDtoCreateResponse(saved, userUuid);
    }

    @Override
    public boolean checkEmail(String email) {
        return profileRepository.existsByEmail(email);
    }

    @Override
    public ProfileDtoUpdateStatus updateStatus(Long userId, UUID userUuid, ProfileStatus profileStatus) {
        Profile profile = Checker.getNonNullObject(profileRepository, userId);
        if (profile.getStatus() == profileStatus) {
            throw new IllegalArgumentException(String.format(
                    "Status is already '%s' for %s with id: #%s", profileStatus, ENTITY_SIMPLE_NAME, userId));
        }
        profile
                .setStatus(profileStatus)
                .setChangedStatusOn(LocalDateTime.now());
        Profile updated = profileRepository.save(profile);
        log.debug("Updated status: '{}' for {} with id: #{}", profileStatus, ENTITY_SIMPLE_NAME, userId);

        return profileMapper.toDtoUpdateStatus(updated, userUuid);
    }
}
