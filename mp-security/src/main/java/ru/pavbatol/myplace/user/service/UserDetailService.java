package ru.pavbatol.myplace.user.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.pavbatol.myplace.user.model.UserAuthenticatedPrincipal;

public interface UserDetailService {
    UserAuthenticatedPrincipal loadUserByLogin(String login) throws UsernameNotFoundException;
}
