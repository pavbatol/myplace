package ru.pavbatol.myplace.security.permission.service;

import ru.pavbatol.myplace.security.permission.dto.UserIdentityDto;

import java.util.List;

public interface PermissionService {
    UserIdentityDto validateAccess(List<String> requiredRoles, String bearerToken, boolean shouldReturnUserId, boolean shouldReturnUserUuid);
}
