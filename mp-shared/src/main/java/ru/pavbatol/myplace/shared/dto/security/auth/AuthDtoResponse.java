package ru.pavbatol.myplace.shared.dto.security.auth;

import lombok.Value;

@Value
public class AuthDtoResponse {
    String type = "Bearer";
    String accessToken;
    String refreshToken;
}
