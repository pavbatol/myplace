package ru.pavbatol.myplace.role.model;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum RoleName implements GrantedAuthority {
    //--Main
    ADMIN,
    USER,

    //--STAFF
    MANAGER,
    MODERATOR,

    //--SHOP
    SHOP_ADMIN,
    SHOP_MANAGER,
    SHOP_MODERATOR,
    SHOP_STOREKEEPER,
    ;

    private static final String PREFIX = "ROLE_";

    @Override
    public String getAuthority() {
        return PREFIX + name();
    }

    public static RoleName of(@NonNull String name) throws IllegalArgumentException {
        try {
            return RoleName.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown name: " + name, e);
        }
    }
}
