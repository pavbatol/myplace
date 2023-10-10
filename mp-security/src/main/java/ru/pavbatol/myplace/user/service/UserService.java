package ru.pavbatol.myplace.user.service;

import ru.pavbatol.myplace.user.dto.UserDtoConfirm;
import ru.pavbatol.myplace.user.dto.UserDtoRegistry;
import ru.pavbatol.myplace.user.dto.UserDtoUpdatePassword;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public interface UserService {
    void register(HttpServletRequest servletRequest, UserDtoRegistry dto);

    void confirmRegistration(UserDtoConfirm dtoConfirm);

    void changePassword(HttpServletRequest servletRequest, UUID userUuid, UserDtoUpdatePassword dto);

    Long getIdByUuid(HttpServletRequest servletRequest, UUID userUuid);
}
