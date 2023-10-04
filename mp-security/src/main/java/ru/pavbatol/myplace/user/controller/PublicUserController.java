package ru.pavbatol.myplace.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.user.dto.UserDtoConfirm;
import ru.pavbatol.myplace.user.dto.UserDtoRegistry;
import ru.pavbatol.myplace.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/users/auth")
@RequiredArgsConstructor
@Tag(name = "Public: User", description = "API for working with User registration")
public class PublicUserController {
    private final UserService userService;

    @PostMapping("/registry")
    @Operation(summary = "register", description = "registering a new user")
    public ResponseEntity<String> register(HttpServletRequest servletRequest, @Valid @RequestBody UserDtoRegistry dtoRegister) {
        log.debug("POST register() with {}", dtoRegister);
        userService.register(servletRequest, dtoRegister);
        String body = "An email with a confirmation code has been sent to your email address.\nConfirm your email address";
        return ResponseEntity.ok(body);
    }

    @PostMapping("/confirmation")
    @Operation(summary = "confirmRegistration", description = "confirming registration")
    public ResponseEntity<String> confirmRegistration(@RequestBody UserDtoConfirm dto) {
        log.debug("POST confirmRegistration() with dto: {}", dto);
        userService.confirmRegistration(dto);
        String body = "Registration is confirmed";
        return ResponseEntity.ok(body);
    }
}