package ru.pavbatol.myplace.auth.model;

import lombok.Value;

@Value
public class AccessTokenDetails {
    String token;
    String ip;
    String osName;
    String browserName;
    String deviceTypeName;
}
