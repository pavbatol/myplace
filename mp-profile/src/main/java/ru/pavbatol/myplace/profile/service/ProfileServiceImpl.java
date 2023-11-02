package ru.pavbatol.myplace.profile.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.profile.dto.ProfileDtoCreateRequest;
import ru.pavbatol.myplace.profile.dto.ProfileDtoCreateResponse;
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
    private final ProfileJpaRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Override
    public ProfileDtoCreateResponse create(UUID userUuid, ProfileDtoCreateRequest createRequest) {
        Profile profile = profileMapper.toEntity(createRequest);
        profile
                .setStatus(ProfileStatus.ACTIVE)
                .setCreatedOn(LocalDateTime.now());
        Profile saved = profileRepository.save(profile);

        return profileMapper.toDtoCreateResponse(saved, userUuid);
    }

    @Override
    public boolean checkEmail(String email) {
        return profileRepository.existsByEmail(email);
    }
}
