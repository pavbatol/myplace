package ru.pavbatol.myplace.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.pavbatol.myplace.dto.annotation.CustomDateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public void setSortDirection(String name) {
        if (name != null) {
            this.sortDirection = SortDirection.valueOf(name.toUpperCase());
        } else {
            this.sortDirection = null;
        }
    }

    public String toQuery(DateTimeFormatter formatter) {
        String startParam = getStart() == null ? "" : "start=" + getStart().format(formatter);
        String endParam = getEnd() == null ? "" : "end=" + getEnd().format(formatter);
        String uniqueParam = getUnique() == null ? "" : "unique=" + getUnique();
        String sortDirectionParam = getSortDirection() == null ? "" : "sortDirection=" + getSortDirection();

        return Stream.of(startParam, endParam, uniqueParam, sortDirectionParam)
                .filter(param -> !param.isEmpty())
                .collect(Collectors.joining("&"));
    }

    protected void ensureNonNullFields() {
        setStart(getStart() != null ? getStart() : START);
        setEnd(getEnd() != null ? getEnd() : END);
        setUnique(getUnique() != null ? getUnique() : UNIQUE);
        setSortDirection(getSortDirection() != null ? getSortDirection().name() : DIRECTION);
    }
}
