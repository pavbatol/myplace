package ru.pavbatol.myplace.app.config;

import lombok.Getter;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Getter
public enum RedisKey {
    USERS_UNVERIFIED_EMAIL("users-unverified-email:"),
    USERS_UNVERIFIED_LOGIN("users-unverified-login:"),
    ;

    private final String key;

    static {
        Set<String> keys = new HashSet<>();
        for (RedisKey value : values()) {
            if (!keys.add(value.getKey())) {
                throw new IllegalArgumentException("Duplicate key value: " + value.getKey());
            }
        }
    }

    RedisKey(@NonNull String newKey) {
        this.key = newKey;
    }
}
