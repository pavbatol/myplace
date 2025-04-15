package ru.pavbatol.myplace.gateway.security.role.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface SecurityRoleClient {
    ResponseEntity<Object> findById(Long roleId, HttpHeaders headers);

    ResponseEntity<Object> findAll(HttpHeaders headers);
}
