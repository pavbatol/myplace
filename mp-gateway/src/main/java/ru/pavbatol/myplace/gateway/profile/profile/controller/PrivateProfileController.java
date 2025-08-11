package ru.pavbatol.myplace.gateway.profile.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.gateway.app.annotation.RequiredRoles;
import ru.pavbatol.myplace.gateway.app.annotation.RequiredHeaders;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.util.HttpUtils;
import ru.pavbatol.myplace.gateway.profile.profile.service.ProfileService;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDto;
import ru.pavbatol.myplace.shared.dto.profile.profile.ProfileDtoUpdate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static ru.pavbatol.myplace.shared.constant.HttpHeaders.X_USER_ID;
import static ru.pavbatol.myplace.shared.constant.HttpHeaders.X_USER_UUID;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/${app.mp.profile.label}/user/profiles")
@RequiredArgsConstructor
@Tag(name = "[Profile/Profile]: Private", description = "API for working with Profile")
public class PrivateProfileController {
    private static final String USER = "USER";
    private final ProfileService profileService;

    @RequiredRoles(roles = {USER}, setUserIdHeader = true)
    @RequiredHeaders(X_USER_ID)
    @DeleteMapping("/{profileId}")
    @Operation(summary = "delete", description = "deleting Profile")
    public Mono<ResponseEntity<Void>> delete(@PathVariable(value = "profileId") Long profileId,
                                             HttpServletRequest request) {
        log.debug("DELETE delete() with profileId: {}", profileId);
        return profileService.delete(profileId, HttpUtils.extractHeaders(request));
    }

    @RequiredRoles(roles = {USER}, setUserIdHeader = true, setUserUuidHeader = true)
    @RequiredHeaders({X_USER_ID, X_USER_UUID})
    @PatchMapping("/{profileId}")
    @Operation(summary = "update", description = "updating Profile")
    public Mono<ResponseEntity<ApiResponse<ProfileDto>>> update(@PathVariable(value = "profileId") Long profileId,
                                                                @RequestBody @Valid ProfileDtoUpdate dto,
                                                                HttpServletRequest request) {
        log.debug("PATCH update() with profileId: {}, dto: {}", profileId, dto);
        Mono<ApiResponse<ProfileDto>> apiResponse = profileService.update(profileId, dto, HttpUtils.extractHeaders(request));
        return apiResponse.map(ar -> ResponseEntity.status(ar.getStatus()).body(ar));
    }

    @RequiredRoles(roles = {USER}, setUserIdHeader = true, setUserUuidHeader = true)
    @RequiredHeaders({X_USER_ID, X_USER_UUID})
    @GetMapping("/{profileId}")
    @Operation(summary = "getById", description = "get Profile")
    public Mono<ResponseEntity<ApiResponse<ProfileDto>>> getById(@PathVariable(value = "profileId") Long profileId,
                                                                 HttpServletRequest request) {
        log.debug("GET getById() with profileId: {}", profileId);
        Mono<ApiResponse<ProfileDto>> apiResponse = profileService.privateGetById(profileId, HttpUtils.extractHeaders(request));
        return apiResponse.map(ar -> ResponseEntity.status(ar.getStatus()).body(ar));
    }

    @RequiredRoles(roles = {USER}, setUserIdHeader = true, setUserUuidHeader = true)
    @RequiredHeaders({X_USER_ID, X_USER_UUID})
    @GetMapping
    @Operation(summary = "getByUserId", description = "get Profile")
    public Mono<ResponseEntity<ApiResponse<ProfileDto>>> getByUserId(HttpServletRequest request) {
        log.debug("GET getByUserId() with userId: {}, userUuid: {}", request.getHeader(X_USER_ID) + " (in headers)", "[expected in headers]");
        Mono<ApiResponse<ProfileDto>> apiResponse = profileService.privateGetByUserId(HttpUtils.extractHeaders(request));
        return apiResponse.map(ar -> ResponseEntity.status(ar.getStatus()).body(ar));
    }
}
