package ru.pavbatol.myplace.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.shared.dto.security.user.UserDtoUpdatePassword;
import ru.pavbatol.myplace.user.service.UserService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
@Tag(name = "Private: User", description = "API for working with User")
public class PrivateUserController {
    private final UserService userService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("/{userUuid}/password")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "changePassword", description = "setting new password")
    public ResponseEntity<Void> changePassword(@PathVariable(value = "userUuid") UUID userUuid,
                                               @RequestBody UserDtoUpdatePassword dto) {
        log.debug("POST changePassword() with userUuid: {}, dto: hidden for security", userUuid);
        userService.changePassword(userUuid, dto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{userUuid}/id")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "getIdByUuid", description = "obtaining User id by UUID")
    public ResponseEntity<Long> getIdByUuid(@PathVariable(value = "userUuid") UUID userUuid) {
        log.debug("POST getIdByUuid() with userUuid: {}", userUuid);
        Long userId = userService.getIdByUuid(userUuid);
        return ResponseEntity.ok(userId);
    }
}