package ru.pavbatol.myplace.gateway.security.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.api.ResponseHandler;
import ru.pavbatol.myplace.gateway.security.user.client.SecurityUserClient;
import ru.pavbatol.myplace.shared.dto.security.user.*;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityUserServiceImpl implements SecurityUserService {
    private final SecurityUserClient client;
    private final ResponseHandler responseHandler;

    @Override
    public ApiResponse<UserDtoResponse> updateRoles(UUID userUuid, UserDtoUpdateRoles dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.updateRoles(userUuid, dto, headers);
        return responseHandler.processResponse(response, UserDtoResponse.class);
    }

    @Override
    public ApiResponse<Void> delete(UUID userUuid, HttpHeaders headers) {
        ResponseEntity<Object> response = client.delete(userUuid, headers);
        return responseHandler.processResponse(response, Void.class);
    }

    @Override
    public ApiResponse<UserDtoResponse> findByUuid(UUID userUuid, HttpHeaders headers) {
        ResponseEntity<Object> response = client.findByUuid(userUuid, headers);
        return responseHandler.processResponse(response, UserDtoResponse.class);
    }

    @Override
    public ApiResponse<List<UserDtoResponse>> findAll(Integer from, Integer size, HttpHeaders headers) {
        ResponseEntity<Object> response = client.findAll(from, size, headers);
        return responseHandler.processResponseList(response, UserDtoResponse.class);
    }

    @Override
    public ApiResponse<Void> changePassword(UUID userUuid, UserDtoUpdatePassword dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.changePassword(userUuid, dto, headers);
        return responseHandler.processResponse(response, Void.class);
    }

    @Override
    public ApiResponse<Long> getIdByUuid(UUID userUuid, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getIdByUuid(userUuid, headers);
        return responseHandler.processResponse(response, Long.class);
    }

    @Override
    public ApiResponse<String> register(UserDtoRegistry dtoRegister, HttpHeaders headers) {
        ResponseEntity<Object> response = client.register(dtoRegister, headers);
        return responseHandler.processResponse(response, String.class);
    }

    @Override
    public ApiResponse<String> confirmRegistration(UserDtoConfirm dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.confirmRegistration(dto, headers);
        return responseHandler.processResponse(response, String.class);
    }
}
