package ru.pavbatol.myplace.gateway.security.user.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import ru.pavbatol.myplace.shared.dto.security.user.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public interface SecurityUserClient {
    ResponseEntity<Object> updateRoles(UUID userUuid, UserDtoUpdateRoles dto, HttpHeaders headers);

    ResponseEntity<Object> delete(UUID userUuid, HttpHeaders headers);

    ResponseEntity<Object> findByUuid(UUID userUuid, HttpHeaders headers);

    ResponseEntity<Object> findAll(Integer from, Integer size, HttpHeaders headers);

    ResponseEntity<Object> changePassword(UUID userUuid, UserDtoUpdatePassword dto, HttpHeaders headers);

    ResponseEntity<Object> getIdByUuid(UUID userUuid, HttpHeaders headers);

    ResponseEntity<Object> register(HttpServletRequest servletRequest, UserDtoRegistry dtoRegister, HttpHeaders headers);

    ResponseEntity<Object> confirmRegistration(UserDtoConfirm dto, HttpHeaders headers);
}
