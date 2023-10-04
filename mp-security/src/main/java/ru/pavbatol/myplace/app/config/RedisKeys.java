package ru.pavbatol.myplace.app.config;

import lombok.Getter;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Getter
public enum RedisKeys {
    USERS_UNVERIFIED("users:unverified");

    private final String key;

    static {
        Set<String> keys = new HashSet<>();
        for (RedisKeys value : values()) {
            if (!keys.add(value.getKey())) {
                throw new IllegalArgumentException("Duplicate key value: " + value.getKey());
            }
        }
    }

    RedisKeys(@NonNull String newKey) {
        this.key = newKey;
    }
}
