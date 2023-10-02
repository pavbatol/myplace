package ru.pavbatol.myplace.app.config;

import lombok.Getter;
import lombok.NonNull;

@Getter
public enum RedisKeys {
    USERS_UNVERIFIED("users:unverified")
    ;

    private final String key;

    RedisKeys(@NonNull String newKey) {
        for (RedisKeys value : values()) {
            if (value.getKey().equals(newKey)) {
                throw new IllegalArgumentException("Duplicate newKey value: " + newKey);
            }
        }
        this.key = newKey;
    }
}
