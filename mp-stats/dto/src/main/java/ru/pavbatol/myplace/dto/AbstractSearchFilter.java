package ru.pavbatol.myplace.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.pavbatol.myplace.dto.annotation.CustomDateTimeFormat;

import javax.validation.constraints.Min;
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
    public static final int PAGE_SIZE = 10;

    @CustomDateTimeFormat
    LocalDateTime start;

    @CustomDateTimeFormat
    LocalDateTime end;

    Boolean unique;

    SortDirection sortDirection;

    @Min(1)
    Integer pageSize;

    public abstract T setNullFieldsToDefault();

    public abstract String toQuery(DateTimeFormatter formatter);

    public void setSortDirection(String name) {
        if (name != null) {
            this.sortDirection = SortDirection.valueOf(name.toUpperCase());
        } else {
            this.sortDirection = null;
        }
    }

    protected final String baseFilterToQuery(DateTimeFormatter formatter) {
        return joinQueryParams(
                toParam("start", getStart() != null ? getStart().format(formatter) : null),
                toParam("end", getEnd() != null ? getEnd().format(formatter) : null),
                toParam("unique", getUnique()),
                toParam("sortDirection", getSortDirection().name())
        );
    }

    protected final String joinQueryParams(String... queryParam) {
        return Arrays.stream(queryParam)
                .filter(Objects::nonNull)
                .filter(params -> !params.isEmpty())
                .collect(Collectors.joining("&"));
    }

    protected final String toParam(String paramName, Object value) {
        if (value == null) {
            return "";
        }
        String format = "%s=%s";
        if (value instanceof List) {
            return String.format(format,
                    paramName,
                    ((List<?>) value).stream()
                            .filter(Objects::nonNull)
                            .map(Object::toString)
                            .collect(Collectors.joining(",")));
        } else if (value instanceof String || value instanceof Integer || value instanceof Long || value instanceof Boolean) {
            return String.format(format, paramName, value);
        } else {
            throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
        }
    }

    protected final void setBaseNullFieldsToDefault() {
        setStart(getStart() != null ? getStart() : START);
        setEnd(getEnd() != null ? getEnd() : END);
        setUnique(getUnique() != null ? getUnique() : UNIQUE);
        setSortDirection(getSortDirection() != null ? getSortDirection().name() : DIRECTION);
        setPageSize(getPageSize() != null ? getPageSize() : PAGE_SIZE);
    }
}
