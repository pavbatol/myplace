package ru.pavbatol.myplace.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.profile.dto.*;
import ru.pavbatol.myplace.profile.service.ProfileService;

import javax.validation.Valid;
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

    @PostMapping
    @Operation(summary = "create", description = "creating new Profile")
    public ResponseEntity<ProfileDtoCreateResponse> create(@RequestHeader(value = X_USER_UUID) UUID userUuid,
                                                           @Valid @RequestBody ProfileDtoCreateRequest dto) {
        log.debug("POST create() with userUuid: {}, dto: {}", dto, userUuid);
        ProfileDtoCreateResponse body = profileService.create(userUuid, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @DeleteMapping("/{profileId}")
    @Operation(summary = "delete", description = "deleting Profile")
    public ResponseEntity<Void> delete(@PathVariable(value = "profileId") Long profileId) {
        log.debug("DELETE delete() with profileId: {}", profileId);
        profileService.delete(profileId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{profileId}")
    @Operation(summary = "update", description = "updating Profile")
    public ResponseEntity<ProfileDto> update(@RequestHeader(value = X_USER_ID) Long userId,
                                             @RequestHeader(value = X_USER_UUID) UUID userUuid,
                                             @PathVariable(value = "profileId") Long profileId,
                                             @RequestBody @Valid ProfileDtoUpdate dto) {
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
        ProfileDto body = profileService.getById(userId, userUuid, profileId);
        return ResponseEntity.ok(body);
    }

    @GetMapping
    @Operation(summary = "getByUserId", description = "get Profile")
    public ResponseEntity<ProfileDto> getByUserId(@RequestHeader(value = X_USER_ID) Long userId,
                                                  @RequestHeader(value = X_USER_UUID) UUID userUuid) {
        log.debug("GET getByUserId() with userId: {}, userUuid: {}", userId, userUuid);
        ProfileDto body = profileService.getByUserId(userId, userUuid);
        return ResponseEntity.ok(body);
    }
}
