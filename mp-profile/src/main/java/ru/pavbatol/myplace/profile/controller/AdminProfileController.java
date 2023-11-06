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
@RequestMapping("/admin/profiles")
@RequiredArgsConstructor
@Tag(name = "Admin: Profile", description = "API for working with Profile")
public class AdminProfileController {
    private final ProfileService profileService;

    @PatchMapping("/status")
    @Operation(summary = "updateStatusByUserId", description = "setting Profile status")
    public ResponseEntity<ProfileDtoUpdateStatusResponse> updateStatusByUserId(@PathVariable(value = "userId") Long userId,
                                                                               @RequestParam(value = "userUuid") UUID userUuid,
                                                                               @RequestParam(value = "status") String status) {
        log.debug("PATCH updateStatusByUserId() with userId: {}, userUuid: {}, status: {}", userId, userUuid, status);
        ProfileStatus profileStatus = EnumUtils.valueOfIgnoreCase(ProfileStatus.class, status);
        ProfileDtoUpdateStatusResponse body = profileService.adminUpdateStatusByUserId(userId, userUuid, profileStatus);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping
    @Operation(summary = "getAll", description = "get all Profiles")
    public ResponseEntity<Slice<ProfileDto>> getAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        log.debug("GET getAll() with page: {}, size: {}", page, size);
        Slice<ProfileDto> body = profileService.adminGetAll(page, size);
        return ResponseEntity.ok(body);
    }
}
