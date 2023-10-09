package ru.pavbatol.myplace.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.user.dto.UserDtoUpdatePassword;
import ru.pavbatol.myplace.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/private/users")
@RequiredArgsConstructor
@Tag(name = "Private: User", description = "API for working with User")
public class PrivateUserController {
    private final UserService userService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{userUuid}/password")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "changePassword", description = "setting new password")
    public ResponseEntity<Void> changePassword(HttpServletRequest servletRequest,
                                               @PathVariable(value = "userUuid") UUID userUuid,
                                               @Valid @RequestBody UserDtoUpdatePassword dto) {
        log.debug("POST changePassword() with userUuid: {}, dto: hidden for security", userUuid);
        userService.changePassword(servletRequest, userUuid, dto);
        return ResponseEntity.ok().build();
    }
}