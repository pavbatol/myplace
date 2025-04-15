package ru.pavbatol.myplace.gateway.security.user.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import ru.pavbatol.myplace.shared.dto.security.user.*;

import java.util.UUID;

/**
 * REST client interface for User Management Service operations.
 * Provides direct communication with the user service API endpoints.
 *
 * <p><b>Key Responsibilities:</b>
 * <ul>
 *   <li>User CRUD operations (create, read, update, delete)</li>
 *   <li>Role management for users</li>
 *   <li>Password and registration workflows</li>
 *   <li>User lookup and pagination</li>
 * </ul>
 *
 * <p><b>Security Context:</b>
 * <ul>
 *   <li>Most operations require admin privileges (JWT in headers)</li>
 *   <li>Registration/confirmation endpoints are public</li>
 *   <li>Password changes require either admin or owner user privileges</li>
 * </ul>
 *
 * <p><b>Response Handling:</b>
 * Returns raw {@link ResponseEntity} objects - response parsing and error handling
 * should be implemented by the caller.
 *
 * @see ru.pavbatol.myplace.gateway.security.user.service.SecurityUserService The business service layer interface
 * @see UserDtoRegistry Registration DTO structure
 * @see UserDtoUpdateRoles Role update DTO structure
 */
public interface SecurityUserClient {
    ResponseEntity<Object> updateRoles(UUID userUuid, UserDtoUpdateRoles dto, HttpHeaders headers);

    ResponseEntity<Object> delete(UUID userUuid, HttpHeaders headers);

    ResponseEntity<Object> findByUuid(UUID userUuid, HttpHeaders headers);

    ResponseEntity<Object> findAll(Integer from, Integer size, HttpHeaders headers);

    ResponseEntity<Object> changePassword(UUID userUuid, UserDtoUpdatePassword dto, HttpHeaders headers);

    ResponseEntity<Object> getIdByUuid(UUID userUuid, HttpHeaders headers);

    ResponseEntity<Object> register(UserDtoRegistry dto, HttpHeaders headers);

    ResponseEntity<Object> confirmRegistration(UserDtoConfirm dto, HttpHeaders headers);
}
