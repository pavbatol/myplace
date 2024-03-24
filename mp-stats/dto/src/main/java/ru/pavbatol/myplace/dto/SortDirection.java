package ru.pavbatol.myplace.dto;

import ru.pavbatol.myplace.dto.annotation.ExcludeJacocoGenerated;

public enum SortDirection {
    ASC,
    DESC;

    @ExcludeJacocoGenerated
    public static SortDirection from(String value) {
        try {
            return SortDirection.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    String.format("Invalid value '%s' for direction given; Has to be either 'desc' or 'asc' (case insensitive)", value), e);
        }
    }
}
