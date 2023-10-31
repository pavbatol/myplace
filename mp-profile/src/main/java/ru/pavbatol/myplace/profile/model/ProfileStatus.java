package ru.pavbatol.myplace.profile.model;

import lombok.NonNull;

public enum ProfileStatus {
    ACTIVE,
    BLOCKED,
    DELETED,
    ;

    public static ProfileStatus of(@NonNull String name) throws IllegalArgumentException {
        try {
            return ProfileStatus.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported name: " + name, e);
        }
    }
}
