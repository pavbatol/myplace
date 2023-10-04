package ru.pavbatol.myplace.user.service;

import ru.pavbatol.myplace.user.dto.UserDtoConfirm;
import ru.pavbatol.myplace.user.dto.UserDtoRegistry;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    void register(HttpServletRequest servletRequest, UserDtoRegistry dto);

    void confirmRegistration(UserDtoConfirm dtoConfirm);
}
