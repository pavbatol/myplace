package ru.pavbatol.myplace.app.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnumUtils {

    public static <T extends Enum<T>> T valueOfIgnoreCase(Class<T> enumClass, String name) throws IllegalArgumentException {
        try {
            return Enum.valueOf(enumClass, name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported name: " + name, e);
        }
    }
}
