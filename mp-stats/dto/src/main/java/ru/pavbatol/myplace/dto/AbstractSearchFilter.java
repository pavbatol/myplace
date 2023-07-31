package ru.pavbatol.myplace.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.pavbatol.myplace.dto.annotation.CustomDateTimeFormat;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractSearchFilter {
    @CustomDateTimeFormat
    LocalDateTime start;

    @CustomDateTimeFormat
    LocalDateTime end;

    Boolean unique;

    SortDirection sortDirection;

    public void setSortDirection(String name) {
        if (name != null) {
            this.sortDirection = SortDirection.valueOf(name.toUpperCase());
        } else {
            this.sortDirection = null;
        }
    }
}
