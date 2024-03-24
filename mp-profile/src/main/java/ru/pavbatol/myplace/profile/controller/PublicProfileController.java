package ru.pavbatol.myplace.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pavbatol.myplace.profile.service.ProfileService;

@Slf4j
@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
@Tag(name = "Public: Profile", description = "API for working with Profile")
public class PublicProfileController {
    private final ProfileService profileService;

    @GetMapping("/check-email")
    @Operation(summary = "checkEmail", description = "checking a profile with this email exists")
    public ResponseEntity<Boolean> checkEmail(@RequestParam(value = "email") String email) {
        log.debug("GET checkEmail() with email: {}", email);
        boolean body = profileService.checkEmail(email);
        return ResponseEntity.ok(body);
    }
}
