package ru.pavbatol.myplace.security.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.shared.dto.security.user.UserDtoConfirm;
import ru.pavbatol.myplace.shared.dto.security.user.UserDtoRegistry;
import ru.pavbatol.myplace.security.user.service.UserService;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
@Tag(name = "Public: User", description = "API for working with User registration")
public class PublicUserController {
    private final UserService userService;

    @PostMapping("/registry")
    @Operation(summary = "register", description = "registering a new user")
    public ResponseEntity<String> register(HttpServletRequest servletRequest, @RequestBody UserDtoRegistry dtoRegister) {
        log.debug("POST register() with email:{}, login: {}, password: hidden for security", dtoRegister.getEmail(), dtoRegister.getLogin());
        String body = userService.register(servletRequest, dtoRegister);
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