package ru.pavbatol.myplace.gateway.security.role.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.api.ResponseHandler;
import ru.pavbatol.myplace.gateway.security.role.client.SecurityRoleClient;
import ru.pavbatol.myplace.shared.dto.security.role.RoleDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityRoleServiceImpl implements SecurityRoleService {
    private final SecurityRoleClient client;
    private final ResponseHandler responseHandler;

    @Override
    public ApiResponse<RoleDto> findById(Long roleId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.findById(roleId, headers);
        return responseHandler.processResponse(response, RoleDto.class);
    }

    @Override
    public ApiResponse<List<RoleDto>> findAll(HttpHeaders headers) {
        ResponseEntity<Object> response = client.findAll(headers);
        return responseHandler.processResponseList(response, RoleDto.class);
    }
}
