package ru.pavbatol.myplace.user.service;

import ru.pavbatol.myplace.user.dto.*;

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

    void register(HttpServletRequest servletRequest, UserDtoRegistry dto);

    void confirmRegistration(UserDtoConfirm dtoConfirm);
}
