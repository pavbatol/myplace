package ru.pavbatol.myplace.auth.model;

import lombok.Value;

import java.io.Serializable;

@Value
public class AccessTokenDetails implements Serializable {
    private static final long serialVersionUID = 100L;
    String token;
    String ip;
    String osName;
    String browserName;
    String deviceTypeName;
}
