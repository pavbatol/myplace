package ru.pavbatol.myplace.gateway.app.access.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessClientImpl implements AccessClient {
    @Value("${app.mp.security.url}")
    private String securityServiceUrl;

    private final RestTemplate restTemplate;

    @Override
    public void checkAccess(List<String> roles, String authToken) {
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Roles list cannot be null or empty");
        }
        if (authToken == null || authToken.isBlank()) {
            throw new IllegalArgumentException("Authorization header is missing");
        }

        log.debug("Checking access for roles: {}", roles);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    URI.create(securityServiceUrl + "/check-access"),
                    HttpMethod.POST,
                    new HttpEntity<>(roles, headers),
                    Void.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new AccessDeniedException("Access denied. Status: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new SecurityException("Security service error: " + e.getMessage(), e);
        }
    }
}
