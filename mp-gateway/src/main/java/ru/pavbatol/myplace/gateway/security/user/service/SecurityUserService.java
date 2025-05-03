package ru.pavbatol.myplace.gateway.security.user.service;

import org.springframework.http.HttpHeaders;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.shared.dto.security.user.*;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for user management operations.
 * Provides business logic for user administration and authentication workflows.
 *
 * <p><b>Core Functionality:</b>
 * <ul>
 *   <li>User lifecycle management (registration, confirmation, deletion)</li>
 *   <li>Role assignment and permission control</li>
 *   <li>Password management</li>
 *   <li>User lookup (single and paginated)</li>
 * </ul>
 *
 * <p><b>Security Requirements:</b>
 * <ul>
 *   <li>Admin operations: Role updates, user deletion, ID lookup</li>
 *   <li>Mixed access: Password changes (admin or owner)</li>
 *   <li>Public operations: Registration and confirmation</li>
 * </ul>
 *
 * <p>All methods return standardized {@link ApiResponse} containing:
 * <ul>
 *   <li>Success: Domain-specific DTO ({@link UserDtoResponse}, etc.)</li>
 *   <li>Failure: Appropriate error code and message</li>
 * </ul>
 *
 * @see ru.pavbatol.myplace.gateway.security.user.client.SecurityUserClient Underlying REST client implementation
 * @see UserDtoResponse User data transfer object
 * @see UserDtoUpdateRoles Role update DTO structure
 */
public interface SecurityUserService {
    ApiResponse<UserDtoResponse> updateRoles(UUID userUuid, UserDtoUpdateRoles dto, HttpHeaders headers);

    ApiResponse<Void> delete(UUID userUuid, HttpHeaders headers);

    ApiResponse<UserDtoResponse> findByUuid(UUID userUuid, HttpHeaders headers);

    ApiResponse<List<UserDtoResponse>> findAll(Integer from, Integer size, HttpHeaders headers);

    ApiResponse<Void> changePassword(UUID userUuid, UserDtoUpdatePassword dto, HttpHeaders headers);

    ApiResponse<Long> getIdByUuid(UUID userUuid, HttpHeaders headers);

    ApiResponse<String> register(UserDtoRegistry dtoRegister, HttpHeaders headers);

    ApiResponse<String> confirmRegistration(UserDtoConfirm dto, HttpHeaders headers);
}
