package ru.pavbatol.myplace.user.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.pavbatol.myplace.user.model.UserAuthenticationPrincipal;

public interface UserDetailService {
    UserAuthenticationPrincipal loadUserByLogin(String login) throws UsernameNotFoundException;
}
