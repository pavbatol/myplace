package ru.pavbatol.myplace.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.profile.service.ProfileService;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDto;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDtoCreateRequest;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDtoCreateResponse;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDtoUpdate;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/user/profiles")
@RequiredArgsConstructor
@Tag(name = "Private: Profile", description = "API for working with Profile")
public class PrivateProfileController {
    private static final String X_USER_ID = "X-User-Id";
    private static final String X_USER_UUID = "X-User-Uuid";
    private final ProfileService profileService;

    /**
     * Creates a new profile with minimal required fields.
     * <p><b>Important:</b> This is an internal API for communication between Security service
     * and Profile service. It's automatically called during user registration process.
     * It should not be called directly from the Gateway service
     *
     * @param userUuid UUID of the user for whom the profile is being created
     * @param dto      Profile creation data transfer object
     * @return ResponseEntity containing the created profile information
     * @apiNote This is an internal API endpoint and not meant for public use
     */
    @PostMapping
    @Operation(summary = "Internal: Create profile",
            description = "INTERNAL USE ONLY. Called by Security service during user registration to create minimal profile",
            hidden = false)
    public ResponseEntity<ProfileDtoCreateResponse> create(@RequestHeader(value = X_USER_UUID) UUID userUuid,
                                                           @RequestBody ProfileDtoCreateRequest dto) {
        log.debug("POST create() with userUuid: {}, dto: {}", dto, userUuid);
        ProfileDtoCreateResponse body = profileService.create(userUuid, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @DeleteMapping("/{profileId}")
    @Operation(summary = "delete", description = "deleting Profile")
    public ResponseEntity<Void> delete(@PathVariable(value = "profileId") Long profileId,
                                       @RequestHeader(value = X_USER_ID) Long userId) {
        log.debug("DELETE delete() with profileId: {}", profileId);
        profileService.delete(profileId, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{profileId}")
    @Operation(summary = "update", description = "updating Profile")
    public ResponseEntity<ProfileDto> update(@RequestHeader(value = X_USER_ID) Long userId,
                                             @RequestHeader(value = X_USER_UUID) UUID userUuid,
                                             @PathVariable(value = "profileId") Long profileId,
                                             @RequestBody ProfileDtoUpdate dto) {
        log.debug("PATCH update() with profileId: {}, userId: {}, userUuid: {}, dto: {}", profileId, userId, userUuid, dto);
        ProfileDto body = profileService.update(userId, userUuid, profileId, dto);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{profileId}")
    @Operation(summary = "getById", description = "get Profile")
    public ResponseEntity<ProfileDto> getById(@RequestHeader(value = X_USER_ID) Long userId,
                                              @RequestHeader(value = X_USER_UUID) UUID userUuid,
                                              @PathVariable(value = "profileId") Long profileId) {
        log.debug("GET getById() with userId: {}, userUuid: {}, profileId: {}", userId, userUuid, profileId);
        ProfileDto body = profileService.privateGetById(userId, userUuid, profileId);
        return ResponseEntity.ok(body);
    }

    @GetMapping
    @Operation(summary = "getByUserId", description = "get Profile")
    public ResponseEntity<ProfileDto> getByUserId(@RequestHeader(value = X_USER_ID) Long userId,
                                                  @RequestHeader(value = X_USER_UUID) UUID userUuid) {
        log.debug("GET getByUserId() with userId: {}, userUuid: {}", userId, userUuid);
        ProfileDto body = profileService.privateGetByUserId(userId, userUuid);
        return ResponseEntity.ok(body);
    }
}
