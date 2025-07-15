package ru.pavbatol.myplace.gateway.profile.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.gateway.app.access.RequiredRoles;
import ru.pavbatol.myplace.gateway.app.annotation.RequiredHeaders;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.profile.profile.service.ProfileService;
import ru.pavbatol.myplace.shared.dto.pagination.PageDto;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDto;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDtoUpdateStatusResponse;
import ru.pavbatol.myplace.shared.enums.profile.profile.ProfileStatus;
import ru.pavbatol.myplace.shared.util.EnumUtils;

import static ru.pavbatol.myplace.shared.constant.HttpHeaders.X_USER_ID;
import static ru.pavbatol.myplace.shared.constant.HttpHeaders.X_USER_UUID;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/${app.mp.profile.label}/admin/profiles")
@RequiredArgsConstructor
@Tag(name = "[Profile/Profile]: Admin", description = "API for working with Profile")
public class AdminProfileController {
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

    @RequiredRoles(roles = {ADMIN})
    @RequiredHeaders({X_USER_ID, X_USER_UUID})
    @GetMapping("/{profileId}")
    @Operation(summary = "getById", description = "get Profile")
    public Mono<ResponseEntity<ApiResponse<ProfileDto>>> getById(@PathVariable(value = "profileId") Long profileId,
                                                                 @RequestHeader HttpHeaders headers) {
        log.debug("GET getById() with profileId: {}", profileId);
        Mono<ApiResponse<ProfileDto>> apiResponse = profileService.adminGetById(profileId, headers);
        return apiResponse.map(ar -> ResponseEntity.status(ar.getStatus()).body(ar));
    }

    @RequiredRoles(roles = {ADMIN})
    @RequiredHeaders({X_USER_ID, X_USER_UUID})
    @GetMapping("/user")
    @Operation(summary = "getByUserId", description = "get Profile")
    public Mono<ResponseEntity<ApiResponse<ProfileDto>>> getByUserId(@RequestHeader HttpHeaders headers) {
        log.debug("GET getByUserId()");
        Mono<ApiResponse<ProfileDto>> apiResponse = profileService.adminGetByUserId(headers);
        return apiResponse.map(ar -> ResponseEntity.status(ar.getStatus()).body(ar));
    }
}
