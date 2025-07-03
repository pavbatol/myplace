package ru.pavbatol.myplace.security.permission.service;

import java.util.List;

public interface PermissionService {
    void validateAccess(List<String> requiredRoles, String bearerToken);
}
