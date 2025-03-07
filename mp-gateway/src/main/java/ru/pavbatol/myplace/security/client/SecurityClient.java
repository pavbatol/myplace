package ru.pavbatol.myplace.security.client;

import java.util.UUID;

public interface SecurityClient {
    void removeRefreshTokensByUserUuid(UUID userUuid);

    void removeAccessTokensByUserUuid(UUID userUuid);

    void clearAuthStorage();
}
