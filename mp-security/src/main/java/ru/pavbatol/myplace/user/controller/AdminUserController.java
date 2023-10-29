package ru.pavbatol.myplace.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.user.dto.UserDtoResponse;
import ru.pavbatol.myplace.user.dto.UserDtoUpdateRoles;
import ru.pavbatol.myplace.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@Tag(name = "Admin: User", description = "API for working with users")
public class AdminUserController {

    private final UserService userService;

    @PatchMapping("/{userUuid}/roles")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "updateRoles", description = "setting new role list")
    public ResponseEntity<UserDtoResponse> updateRoles(@PathVariable("userUuid") UUID userUuid,
                                                       @RequestBody @Valid UserDtoUpdateRoles dto) {
        log.debug("PUT updateRoles() with userUuid: {}, dto: {}", userUuid, dto);
        UserDtoResponse body = userService.updateRoles(userUuid, dto);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{userUuid}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "delete", description = "deleting a user (marks as deleted)")
    public ResponseEntity<Void> delete(@PathVariable("userUuid") UUID userUuid) {
        log.debug("DELETE delete() with userUuid {}", userUuid);
        userService.delete(userUuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userUuid}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "findByUuid", description = "getting a user by Id")
    public ResponseEntity<UserDtoResponse> findByUuid(@PathVariable("userUuid") UUID userUuid) {
        log.debug("GET findByUuid() with userUuid {}", userUuid);
        UserDtoResponse body = userService.findByUuid(userUuid);
        return ResponseEntity.ok(body);
    }

    @GetMapping
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "findAll", description = "getting users page by page")
    public ResponseEntity<List<UserDtoResponse>> findAll(@RequestParam(value = "from", defaultValue = "0") Integer from,
                                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.debug("GET findAll()");
        List<UserDtoResponse> body = userService.findAll(from, size);
        return ResponseEntity.ok(body);
    }
}
