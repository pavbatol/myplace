package ru.pavbatol.myplace.shared.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpHeaders {
    public static final String X_USER_ID = "X-User-Id";
    public static final String X_USER_UUID = "X-User-Uuid";
    public static final String AUTHORIZATION = "Authorization";
    public static final String USER_AGENT = "User-Agent";
    public static final String CONTENT_TYPE = "Content-Type";
}
