package ru.pavbatol.myplace.gateway.profile.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.gateway.app.access.RequiredRoles;
import ru.pavbatol.myplace.gateway.app.annotation.RequiredHeaders;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.profile.profile.service.ProfileService;
import ru.pavbatol.myplace.shared.dto.pagination.PageDto;
import ru.pavbatol.myplace.shared.dto.pagination.SliceDto;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDto;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDtoUpdateStatusResponse;
import ru.pavbatol.myplace.shared.enums.profile.profile.ProfileStatus;
import ru.pavbatol.myplace.shared.util.EnumUtils;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/${app.mp.profile.label}/admin/profiles")
@RequiredArgsConstructor
@Tag(name = "[Profile/Profile]: Admin", description = "API for working with Profile")
public class AdminProfileController {
    private static final String X_USER_ID = "X-User-Id";
    private static final String X_USER_UUID = "X-User-Uuid";
    private static final String ADMIN = "ADMIN";
    private final ProfileService profileService;

    @RequiredRoles(roles = {ADMIN})
    @RequiredHeaders({X_USER_ID, X_USER_UUID})
    @PatchMapping("/status")
    @Operation(summary = "updateStatusByUserId", description = "setting Profile status")
    public Mono<ResponseEntity<ApiResponse<ProfileDtoUpdateStatusResponse>>> updateStatusByUserId(@RequestParam(value = "status") String status,
                                                                                                  @RequestHeader HttpHeaders headers) {
        log.debug("PATCH updateStatusByUserId() with {} header: {}, {}} header: {}, status: {}",
                X_USER_ID, headers.getFirst(X_USER_ID), X_USER_UUID, headers.getFirst(X_USER_UUID), status);
        ProfileStatus profileStatus = EnumUtils.valueOfIgnoreCase(ProfileStatus.class, status);

        Mono<ApiResponse<ProfileDtoUpdateStatusResponse>> apiResponse = profileService.adminUpdateStatusByUserId(profileStatus, headers);
        return apiResponse.map(ar -> ResponseEntity.status(ar.getStatus()).body(ar));
    }

    @RequiredRoles(roles = {ADMIN})
    @GetMapping
    @Operation(summary = "getAll", description = "get all Profiles")
    public Mono<ResponseEntity<ApiResponse<PageDto<ProfileDto>>>> getAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                         @RequestParam(value = "size", defaultValue = "10") int size,
                                                                         @RequestHeader HttpHeaders headers) {
        log.debug("GET getAll() with page: {}, size: {}", page, size);
        Mono<ApiResponse<PageDto<ProfileDto>>> apiResponse = profileService.adminGetAll(page, size, headers);
        return apiResponse.map(ar -> ResponseEntity.status(ar.getStatus()).body(ar));
    }

//    @GetMapping("/{profileId}")
//    @Operation(summary = "getById", description = "get Profile")
//    public ResponseEntity<ProfileDto> getById(@RequestHeader(value = X_USER_ID) Long userId,
//                                              @RequestHeader(value = X_USER_UUID) UUID userUuid,
//                                              @PathVariable(value = "profileId") Long profileId) {
//        log.debug("GET getById() with userId: {}, userUuid: {}, profileId: {}", userId, userUuid, profileId);
//        ProfileDto body = profileService.adminGetById(userId, userUuid, profileId);
//        return ResponseEntity.ok(body);
//    }
//
//    @GetMapping({"/byuserid", "/byUserId"})
//    @Operation(summary = "getByUserId", description = "get Profile")
//    public ResponseEntity<ProfileDto> getByUserId(@RequestHeader(value = X_USER_ID) Long userId,
//                                                  @RequestHeader(value = X_USER_UUID) UUID userUuid) {
//        log.debug("GET getByUserId() with userId: {}, userUuid: {}", userId, userUuid);
//        ProfileDto body = profileService.adminGetByUserId(userId, userUuid);
//        return ResponseEntity.ok(body);
//    }
}
