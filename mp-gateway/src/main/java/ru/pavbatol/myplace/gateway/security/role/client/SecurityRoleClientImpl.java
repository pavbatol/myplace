package ru.pavbatol.myplace.gateway.security.role.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.api.ResponseHandler;
import ru.pavbatol.myplace.shared.client.BaseRestClient;
import ru.pavbatol.myplace.shared.dto.security.role.RoleDto;

import java.util.List;

@Slf4j
@Service
public class SecurityRoleClientImpl extends BaseRestClient implements SecurityRoleClient {
    private static final String ADMIN_ROLE_CONTEXT = "/admin/roles";
    private final ResponseHandler responseHandler;

    public SecurityRoleClientImpl(@Value("${app.mp.security.url}") String serverUrl,
                                  RestTemplateBuilder builder,
                                  ResponseHandler responseHandler) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());

        this.responseHandler = responseHandler;
    }

    @Override
    public ResponseEntity<ApiResponse<RoleDto>> findById(Long roleId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", roleId);
        String fullResourcePath = ADMIN_ROLE_CONTEXT + resourcePath;

        ResponseEntity<Object> response = get(fullResourcePath, headers);
        ApiResponse<RoleDto> apiResponse = responseHandler.processResponse(response, RoleDto.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<List<RoleDto>>> findAll(HttpHeaders headers) {
        ResponseEntity<Object> response = get(ADMIN_ROLE_CONTEXT, headers);
        ApiResponse<List<RoleDto>> apiResponse = responseHandler.processResponseList(response, RoleDto.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }
}
