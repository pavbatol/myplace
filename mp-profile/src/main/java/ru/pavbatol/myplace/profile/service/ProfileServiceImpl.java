package ru.pavbatol.myplace.profile.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.app.Util.Checker;
import ru.pavbatol.myplace.app.exception.NotFoundException;
import ru.pavbatol.myplace.profile.dto.*;
import ru.pavbatol.myplace.profile.mapper.ProfileMapper;
import ru.pavbatol.myplace.profile.model.Profile;
import ru.pavbatol.myplace.profile.model.ProfileStatus;
import ru.pavbatol.myplace.profile.repository.ProfileJpaRepository;

import java.time.LocalDateTime;
import java.util.Objects;
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
    public ProfileDtoUpdateStatusResponse adminUpdateStatusByUserId(Long userId, UUID userUuid, ProfileStatus status) {
        Profile profile = getNonNullProfileByUserId(userId);
        if (profile.getStatus() == status) {
            throw new IllegalArgumentException(String.format(
                    "Status is already '%s' for %s with id: #%s", status, ENTITY_SIMPLE_NAME, userUuid));
        }
        profile
                .setStatus(status)
                .setChangedStatusOn(LocalDateTime.now());
        Profile updated = profileRepository.save(profile);
        log.debug("Updated status: '{}' for {} with id: #{}", status, ENTITY_SIMPLE_NAME, userId);

        return profileMapper.toDtoUpdateStatusResponse(updated, userUuid);
    }

    @Override
    public ProfileDto update(Long userId, UUID userUuid, Long profileId, ProfileDtoUpdate dto) {
        Profile profile = Checker.getNonNullObject(profileRepository, profileId);
        checkUserIdOwnership(userId, profile.getUserId());
        Profile updated = profileMapper.updateEntity(profile, dto, userId);
        if (updated.getAvatar() != null && updated.getAvatar().length > 2 * 1024 * 1024) {
            throw new IllegalArgumentException("The size of the avatar image is too large: " + updated.getAvatar().length + "b");
        }
        updated = profileRepository.save(updated);
        log.debug("Updated {}: {}", ENTITY_SIMPLE_NAME, updated);

        return profileMapper.toProfileDto(updated, userUuid);
    }

    @Override
    public void delete(Long profileId) {
        Profile profile = Checker.getNonNullObject(profileRepository, profileId);
        profile
                .setStatus(ProfileStatus.DELETED)
                .setChangedStatusOn(LocalDateTime.now());
        profileRepository.save(profile);
        log.debug("Updated status: '{}' for {} with id: #{}", ProfileStatus.DELETED, ENTITY_SIMPLE_NAME, profileId);
    }

    @Override
    public ProfileDto getById(Long userId, UUID userUuid, Long profileId) {
        Profile profile = Checker.getNonNullObject(profileRepository, profileId);
        checkUserIdOwnership(userId, profile.getUserId());
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, profile);

        return profileMapper.toProfileDto(profile, userUuid);
    }

    @Override
    public ProfileDto getByUserId(Long userId, UUID userUuid) {
        Profile found = getNonNullProfileByUserId(userId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);

        return profileMapper.toProfileDto(found, userUuid);
    }

    @Override
    public Slice<ProfileDto> adminGetAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        Slice<Profile> found = profileRepository.findAll(pageRequest);
        log.debug("Found Slice of {}: {}, numberOfElements: {}", ENTITY_SIMPLE_NAME, found, found.getNumberOfElements());

        return found.map(profileMapper::toProfileDto);
    }

    private Profile getNonNullProfileByUserId(Long userId) {
        return profileRepository.findByUserId(userId).orElseThrow(() ->
                new NotFoundException(String.format("%s with userId #%s was not found", ENTITY_SIMPLE_NAME, userId)));
    }

    private void checkUserIdOwnership(Long requesterId, Long ownerId) {
        if (!Objects.equals(requesterId, ownerId)) {
            log.debug("You do not have access to other people's data, requesterId: {}, ownerId: {}", requesterId, ownerId);
            throw new IllegalArgumentException("You do not have access to other people's data.");
        }
    }
}
