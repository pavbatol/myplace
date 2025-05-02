package ru.pavbatol.myplace.security.user.service;

import ru.pavbatol.myplace.shared.dto.security.user.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

public interface UserService {
    void changePassword(UUID userUuid, UserDtoUpdatePassword dto);

    UserDtoResponse updateRoles(UUID userUuid, UserDtoUpdateRoles dto);

    void delete(UUID userUuid);

    Long getIdByUuid(UUID userUuid);

    UserDtoResponse findByUuid(UUID userUuid);

    List<UserDtoResponse> findAll(Integer from, Integer size);

    String register(HttpServletRequest servletRequest, UserDtoRegistry dto);

    void confirmRegistration(UserDtoConfirm dtoConfirm);
}
