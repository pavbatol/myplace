package ru.pavbatol.myplace.security.user.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.pavbatol.myplace.security.user.model.UserAuthenticationPrincipal;

public interface UserDetailService {
    UserAuthenticationPrincipal loadUserByLogin(String login) throws UsernameNotFoundException;
}
