package ru.pavbatol.myplace.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.pavbatol.myplace.dto.annotation.CustomDateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public abstract String toQuery(DateTimeFormatter formatter);

    public void setSortDirection(String name) {
        if (name != null) {
            this.sortDirection = SortDirection.valueOf(name.toUpperCase());
        } else {
            this.sortDirection = null;
        }
    }

    protected final String baseFilterToQuery(DateTimeFormatter formatter) {
        String startParam = getStart() == null ? "" : "start=" + getStart().format(formatter);
        String endParam = getEnd() == null ? "" : "end=" + getEnd().format(formatter);
        String uniqueParam = getUnique() == null ? "" : "unique=" + getUnique();
        String sortDirectionParam = getSortDirection() == null ? "" : "sortDirection=" + getSortDirection();

        return joinQueryParams(startParam, endParam, uniqueParam, sortDirectionParam);
    }

    protected final String joinQueryParams(String... queryParam) {
        return Arrays.stream(queryParam)
                .filter(params -> !params.isEmpty())
                .collect(Collectors.joining("&"));
    }

    protected final String toParam(String paramName, String value) {
        return value == null ? "" : String.format("%s=%s", paramName, value);
    }

    protected final String toParamFromStrings(String paramName, List<String> values) {
        return values == null ? "" : String.format("%s=%s", paramName, values.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining(",")));
    }

    protected final String toParamFromLongs(String paramName, List<Long> values) {
        return values == null ? "" : String.format("%s=%s", paramName, values.stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .collect(Collectors.joining(",")));
    }

    protected final void ensureNonNullBaseFields() {
        setStart(getStart() != null ? getStart() : START);
        setEnd(getEnd() != null ? getEnd() : END);
        setUnique(getUnique() != null ? getUnique() : UNIQUE);
        setSortDirection(getSortDirection() != null ? getSortDirection().name() : DIRECTION);
    }
}
