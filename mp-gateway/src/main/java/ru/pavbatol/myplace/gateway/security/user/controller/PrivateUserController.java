package ru.pavbatol.myplace.gateway.security.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.security.user.service.SecurityUserService;
import ru.pavbatol.myplace.shared.dto.security.user.UserDtoUpdatePassword;

import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/${app.mp.security.label}/users")
@RequiredArgsConstructor
@Tag(name = "Private: User", description = "API for working with User")
public class PrivateUserController {
    private final SecurityUserService service;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("/{userUuid}/password")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "changePassword", description = "setting new password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@PathVariable(value = "userUuid") UUID userUuid,
                                                            @Valid @RequestBody UserDtoUpdatePassword dto) {
        log.debug("POST changePassword() with userUuid: {}, dto: hidden for security", userUuid);
        ApiResponse<Void> apiResponse = service.changePassword(userUuid, dto);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{userUuid}/id")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "getIdByUuid", description = "obtaining User id by UUID")
    public ResponseEntity<ApiResponse<Long>> getIdByUuid(@PathVariable(value = "userUuid") UUID userUuid) {
        log.debug("POST getIdByUuid() with userUuid: {}", userUuid);
        ApiResponse<Long> apiResponse = service.getIdByUuid(userUuid);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}