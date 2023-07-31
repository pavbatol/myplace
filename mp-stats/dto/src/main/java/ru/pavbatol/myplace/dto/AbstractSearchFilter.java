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
public abstract class AbstractSearchFilter<T extends AbstractSearchFilter<T>> {
    private static final LocalDateTime START = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
    private static final LocalDateTime END = LocalDateTime.now();
    private static final boolean UNIQUE = false;
    private static final String DIRECTION = SortDirection.DESC.name();

    @CustomDateTimeFormat
    LocalDateTime start;

    @CustomDateTimeFormat
    LocalDateTime end;

    Boolean unique;

    SortDirection sortDirection;

    public abstract T populateNullFields();

    protected void ensureNonNullFields() {
        setStart(getStart() != null ? getStart() : START);
        setEnd(getEnd() != null ? getEnd() : END);
        setUnique(getUnique() != null ? getUnique() : UNIQUE);
        setSortDirection(getSortDirection() != null ? getSortDirection().name() : DIRECTION);
    }

    public void setSortDirection(String name) {
        if (name != null) {
            this.sortDirection = SortDirection.valueOf(name.toUpperCase());
        } else {
            this.sortDirection = null;
        }
    }
}
