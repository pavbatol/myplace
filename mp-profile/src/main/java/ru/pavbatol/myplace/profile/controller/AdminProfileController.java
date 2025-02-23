package ru.pavbatol.myplace.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.app.Util.EnumUtils;
import ru.pavbatol.myplace.profile.dto.ProfileDto;
import ru.pavbatol.myplace.profile.dto.ProfileDtoUpdateStatusResponse;
import ru.pavbatol.myplace.profile.model.ProfileStatus;
import ru.pavbatol.myplace.profile.service.ProfileService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/profiles")
@RequiredArgsConstructor
@Tag(name = "Admin: Profile", description = "API for working with Profile")
public class AdminProfileController {
    private static final String X_USER_ID = "X-User-Id";
    private static final String X_USER_UUID = "X-User-Uuid";
    private final ProfileService profileService;

    @PatchMapping("/status")
    @Operation(summary = "updateStatusByUserId", description = "setting Profile status")
    public ResponseEntity<ProfileDtoUpdateStatusResponse> updateStatusByUserId(@RequestHeader(value = X_USER_ID) Long userId,
                                                                               @RequestHeader(value = X_USER_UUID) UUID userUuid,
                                                                               @RequestParam(value = "status") String status) {
        log.debug("PATCH updateStatusByUserId() with userId: {}, userUuid: {}, status: {}", userId, userUuid, status);
        ProfileStatus profileStatus = EnumUtils.valueOfIgnoreCase(ProfileStatus.class, status);
        ProfileDtoUpdateStatusResponse body = profileService.adminUpdateStatusByUserId(userId, userUuid, profileStatus);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping
    @Operation(summary = "getAll", description = "get all Profiles")
    public ResponseEntity<Slice<ProfileDto>> getAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        log.debug("GET getAll() with page: {}, size: {}", page, size);
        Slice<ProfileDto> body = profileService.adminGetAll(page, size);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{profileId}")
    @Operation(summary = "getById", description = "get Profile")
    public ResponseEntity<ProfileDto> getById(@RequestHeader(value = X_USER_ID) Long userId,
                                              @RequestHeader(value = X_USER_UUID) UUID userUuid,
                                              @PathVariable(value = "profileId") Long profileId) {
        log.debug("GET getById() with userId: {}, userUuid: {}, profileId: {}", userId, userUuid, profileId);
        ProfileDto body = profileService.adminGetById(userId, userUuid, profileId);
        return ResponseEntity.ok(body);
    }

    @GetMapping({"/byuserid", "/byUserId"})
    @Operation(summary = "getByUserId", description = "get Profile")
    public ResponseEntity<ProfileDto> getByUserId(@RequestHeader(value = X_USER_ID) Long userId,
                                                  @RequestHeader(value = X_USER_UUID) UUID userUuid) {
        log.debug("GET getByUserId() with userId: {}, userUuid: {}", userId, userUuid);
        ProfileDto body = profileService.adminGetByUserId(userId, userUuid);
        return ResponseEntity.ok(body);
    }
}
