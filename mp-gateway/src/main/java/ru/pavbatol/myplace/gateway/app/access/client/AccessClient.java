package ru.pavbatol.myplace.gateway.app.access.client;

import java.util.List;

public interface AccessClient {
    void checkAccess(List<String> roles, String authToken, String userAgent);
}
