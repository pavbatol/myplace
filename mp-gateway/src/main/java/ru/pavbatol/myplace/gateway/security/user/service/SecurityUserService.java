package ru.pavbatol.myplace.gateway.security.user.service;

import org.springframework.http.HttpHeaders;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.shared.dto.security.user.*;

import java.util.List;
import java.util.UUID;

public interface SecurityUserService {
    ApiResponse<UserDtoResponse> updateRoles(UUID userUuid, UserDtoUpdateRoles dto, HttpHeaders headers);

    ApiResponse<Void> delete(UUID userUuid, HttpHeaders headers);

    ApiResponse<UserDtoResponse> findByUuid(UUID userUuid, HttpHeaders headers);

    ApiResponse<List<UserDtoResponse>> findAll(Integer from, Integer size, HttpHeaders headers);

    ApiResponse<Void> changePassword(UUID userUuid, UserDtoUpdatePassword dto, HttpHeaders headers);

    ApiResponse<Long> getIdByUuid(UUID userUuid, HttpHeaders headers);

    ApiResponse<String> register(UserDtoRegistry dtoRegister, HttpHeaders headers);

    ApiResponse<String> confirmRegistration(UserDtoConfirm dto, HttpHeaders headers);
}
