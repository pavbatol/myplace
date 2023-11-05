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
@RequestMapping("/profiles")
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

    @GetMapping("/check-email")
    @Operation(summary = "checkEmail", description = "checking a profile with this email exists")
    public ResponseEntity<Boolean> checkEmail(@RequestParam(value = "email") String email) {
        log.debug("GET checkEmail() with email: {}", email);
        boolean body = profileService.checkEmail(email);
        return ResponseEntity.ok(body);
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
                                             @Valid @RequestBody ProfileDtoUpdate dto) {
        log.debug("PATCH update() with profileId: {}, userId: {}, userUuid: {}, dto: {}", profileId, userId, userUuid, dto);
        ProfileDto body = profileService.update(userId, userUuid, profileId, dto);
        return ResponseEntity.ok(body);
    }
}
