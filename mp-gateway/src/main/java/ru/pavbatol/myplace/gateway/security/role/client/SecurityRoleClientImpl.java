package ru.pavbatol.myplace.gateway.security.role.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.pavbatol.myplace.shared.client.BaseRestClient;

/**
 * REST client implementation for Security Role Service API.
 * Provides CRUD operations for role management in the system.
 *
 * <p>Supported operations:</p>
 * <ul>
 *   <li><b>Role retrieval:</b> Get all roles or single role by ID</li>
 * </ul>
 *
 * <p><b>Security context:</b> All operations require {@value #ADMIN_ROLE_CONTEXT} privileges.</p>
 *
 * <p>Extends {@link BaseRestClient} for core REST functionality.</p>
 *
 * @see SecurityRoleClient
 */
@Slf4j
@Service
public class SecurityRoleClientImpl extends BaseRestClient implements SecurityRoleClient {
    private static final String ADMIN_ROLE_CONTEXT = "/admin/roles";

    public SecurityRoleClientImpl(@Value("${app.mp.security.url}") String serverUrl,
                                  @Autowired RestTemplate restTemplate) {
        super(restTemplate, serverUrl);
    }

    @Override
    public ResponseEntity<Object> findById(Long roleId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", roleId);
        String fullResourcePath = ADMIN_ROLE_CONTEXT + resourcePath;

        return get(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> findAll(HttpHeaders headers) {
        return get(ADMIN_ROLE_CONTEXT, headers);
    }
}
