package ru.pavbatol.myplace.gateway.security.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.security.user.service.SecurityUserService;
import ru.pavbatol.myplace.shared.dto.security.user.UserDtoConfirm;
import ru.pavbatol.myplace.shared.dto.security.user.UserDtoRegistry;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/${app.mp.security.label}/auth")
@RequiredArgsConstructor
@Tag(name = "Public: User", description = "API for working with User registration")
public class PublicUserController {
    private final SecurityUserService service;

    @PostMapping("/registry")
    @Operation(summary = "register", description = "registering a new user")
    public ResponseEntity<ApiResponse<String>> register(HttpServletRequest servletRequest,
                                                        @Valid @RequestBody UserDtoRegistry dtoRegister,
                                                        @RequestHeader HttpHeaders headers) {
        log.debug("POST register() with email:{}, login: {}, password: hidden for security", dtoRegister.getEmail(), dtoRegister.getLogin());
        ApiResponse<String> apiResponse = service.register(servletRequest, dtoRegister, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PostMapping("/confirmation")
    @Operation(summary = "confirmRegistration", description = "confirming registration")
    public ResponseEntity<ApiResponse<String>> confirmRegistration(@Valid @RequestBody UserDtoConfirm dto,
                                                                   @RequestHeader HttpHeaders headers) {
        log.debug("POST confirmRegistration() with dto: {}", dto);
        ApiResponse<String> apiResponse = service.confirmRegistration(dto, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}