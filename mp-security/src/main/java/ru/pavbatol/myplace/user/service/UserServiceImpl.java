package ru.pavbatol.myplace.user.service;

import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.user.dto.UserDtoRegistry;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public void register(HttpServletRequest servletRequest, UserDtoRegistry dto) {

    }

    @Override
    public void confirmRegistration(String code) {

    }
}
