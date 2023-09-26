package ru.pavbatol.myplace.role.model;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum RoleName implements GrantedAuthority {
    //--GLOBAL
    MP_ADMIN,
    MP_EMPLOYEE_MODERATOR,
    MP_EMPLOYEE_STOREKEEPER,
    MP_EMPLOYEE_ISSUING_ORDERS_MAN,

    //--VENDOR
    VENDOR_ADMIN,
    VENDOR_EMPLOYEE_MANAGER,
    VENDOR_EMPLOYEE_MODERATOR,
    VENDOR_EMPLOYEE_STOREKEEPER,

    //--CONSUMER
    USER;

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
