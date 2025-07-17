package ru.pavbatol.myplace.gateway.app.access.client;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccessClient {
    ResponseEntity<Void> checkAccess(List<String> roles, String authToken, String userAgent);
}
