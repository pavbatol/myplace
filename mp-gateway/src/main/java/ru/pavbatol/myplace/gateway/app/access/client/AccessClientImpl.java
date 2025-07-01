package ru.pavbatol.myplace.gateway.app.access.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Client implementation for performing access control checks forwarding requests to target services.
 *
 * <p>This component is responsible for communicating with the remote permission service
 * to verify if the current user (identified by the auth token) has the required roles.</p>
 *
 * <p><b>Usage Example:</b>
 * <pre>{@code
 * accessClient.checkAccess(List.of("ADMIN", "EDITOR"), "Bearer token");
 * }</pre>
 */
@Slf4j
@Component
public class AccessClientImpl implements AccessClient {
    private static final String CHECK_ACCESS_PATH = "/permission/check-access";
    private static final String AUTHORIZATION = "Authorization";
    private static final String USER_AGENT = "User-Agent";
    private final String securityServiceUrl;
    private final RestTemplate restTemplate;

    /**
     * Constructs a new AccessClient with the specified security service URL and RestTemplate factory.
     *
     * @param securityServiceUrl the base URL of the security service
     * @param restTemplate       RestTemplate client
     */
    public AccessClientImpl(@Value("${app.mp.security.url}") String securityServiceUrl,
                            @Autowired RestTemplate restTemplate) {
        this.securityServiceUrl = securityServiceUrl;
        this.restTemplate = restTemplate;
    }

    /**
     * Verifies if the authenticated user has at least one of the required roles.
     *
     * <p>This method performs the following operations:
     * <ol>
     *   <li>Validates input parameters</li>
     *   <li>Sets up authorization headers</li>
     *   <li>Sends a request to the security service</li>
     *   <li>Propagates any errors from the security service</li>
     * </ol>
     *
     * @param roles     the list of required roles to check against (must not be null or empty)
     * @param authToken the authorization token in "Bearer [token]" format (must not be null or blank)
     * @param userAgent the User-Agent value
     * @throws IllegalArgumentException if roles list is empty/null or auth token is missing
     * @see AccessClient#checkAccess(List, String, String)
     */
    @Override
    public void checkAccess(List<String> roles, String authToken, String userAgent) {
        log.debug("Sending request for check access for roles: {}", roles);

        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, authToken);

        if (userAgent != null) {
            headers.set(USER_AGENT, userAgent);
        }

        restTemplate.exchange(
                securityServiceUrl + CHECK_ACCESS_PATH,
                HttpMethod.POST,
                new HttpEntity<>(roles, headers),
                Void.class
        );
    }
}
