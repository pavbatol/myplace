package ru.pavbatol.myplace.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.pavbatol.myplace.role.model.Role;
import ru.pavbatol.myplace.user.model.User;
import ru.pavbatol.myplace.user.model.UserAuthenticatedPrincipal;
import ru.pavbatol.myplace.user.repository.UserJpaRepository;
import ru.pavbatol.myplace.user.service.UserDetailService;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailService {
    private final UserJpaRepository repository;

    @Override
    public UserAuthenticatedPrincipal loadUserByLogin(String login) throws UsernameNotFoundException {
        User user = repository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(
                String.format("%s with login: %s not found", User.class.getSimpleName(), login)));
        return new UserAuthenticatedPrincipal(
                user.getId(),
                user.getUuid(),
                user.getLogin(),
                user.getRoles().stream()
                        .map(Role::getRoleName)
                        .collect(Collectors.toSet()),
                user.getDeleted() != null && !user.getDeleted()
        );
    }
}
