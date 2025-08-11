package ru.pavbatol.myplace.gateway.profile.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.profile.profile.service.ProfileService;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/${app.mp.profile.label}/profiles")
@RequiredArgsConstructor
@Tag(name = "[Profile/Profile]: Public", description = "API for working with Profile")
public class PublicProfileController {
    private final ProfileService profileService;

    @GetMapping("/check-email")
    @Operation(summary = "checkEmail", description = "checking a profile with this email exists")
    public Mono<ResponseEntity<ApiResponse<Boolean>>> checkEmail(@RequestParam(value = "email") String email) {
        log.debug("GET checkEmail() with email: {}", email);
        Mono<ApiResponse<Boolean>> apiResponse = profileService.checkEmail(email);
        return apiResponse.map(ar -> ResponseEntity.status(ar.getStatus()).body(ar));
    }
}
