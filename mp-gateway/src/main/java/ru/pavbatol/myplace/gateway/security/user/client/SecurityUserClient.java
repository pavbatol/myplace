package ru.pavbatol.myplace.gateway.security.user.client;

import org.springframework.http.ResponseEntity;
import ru.pavbatol.myplace.shared.dto.security.user.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public interface SecurityUserClient {
    ResponseEntity<Object> updateRoles(UUID userUuid, UserDtoUpdateRoles dto);

    ResponseEntity<Object> delete(UUID userUuid);

    ResponseEntity<Object> findByUuid(UUID userUuid);

    ResponseEntity<Object> findAll(Integer from, Integer size);

    ResponseEntity<Object> changePassword(UUID userUuid, UserDtoUpdatePassword dto);

    ResponseEntity<Object> getIdByUuid(UUID userUuid);

    ResponseEntity<Object> register(HttpServletRequest servletRequest, UserDtoRegistry dtoRegister);

    ResponseEntity<Object> confirmRegistration(UserDtoConfirm dto);
}
