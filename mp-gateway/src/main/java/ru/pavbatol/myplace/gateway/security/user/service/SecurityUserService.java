package ru.pavbatol.myplace.gateway.security.user.service;

import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.shared.dto.security.user.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

public interface SecurityUserService {
    ApiResponse<UserDtoResponse> updateRoles(UUID userUuid, UserDtoUpdateRoles dto);

    ApiResponse<Void> delete(UUID userUuid);

    ApiResponse<UserDtoResponse> findByUuid(UUID userUuid);

    ApiResponse<List<UserDtoResponse>> findAll(Integer from, Integer size);

    ApiResponse<Void> changePassword(UUID userUuid, UserDtoUpdatePassword dto);

    ApiResponse<Long> getIdByUuid(UUID userUuid);

    ApiResponse<String> register(HttpServletRequest servletRequest, UserDtoRegistry dtoRegister);

    ApiResponse<String> confirmRegistration(UserDtoConfirm dto);
}
