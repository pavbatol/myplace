package ru.pavbatol.myplace.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.user.dto.UserDtoRegistry;
import ru.pavbatol.myplace.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Public: User", description = "API for working with User registration")
public class PublicUserController {
    public static final String REGISTRY_PATH = "/registry";
    public static final String CONFIRMATION_PATH = "/confirmation";
    public static final String CODE = "code";
    private final UserService userService;

    @PostMapping(REGISTRY_PATH)
    @Operation(summary = "register", description = "registering a new user")
    public ResponseEntity<String> register(HttpServletRequest servletRequest, @Valid @RequestBody UserDtoRegistry dtoRegister) {
        log.debug("POST register() with {}", dtoRegister);
        userService.register(servletRequest, dtoRegister);
        String body = "An email with a confirmation code has been sent to your email address.\nConfirm your email address";
        return ResponseEntity.ok(body);
    }

    @GetMapping(CONFIRMATION_PATH)
    @Operation(summary = "confirmRegistration", description = "endpoint to confirm registration")
    public ResponseEntity<String> confirmRegistration(@RequestParam(CODE) String code) {
        log.debug("GET confirmRegistration() with {}: {}", CODE, code);
        userService.confirmRegistration(code);
        String body = "Registration is confirmed";
        return ResponseEntity.ok(body);
    }
}