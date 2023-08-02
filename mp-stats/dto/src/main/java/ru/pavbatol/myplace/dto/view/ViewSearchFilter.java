package ru.pavbatol.myplace.dto.view;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.pavbatol.myplace.dto.AbstractSearchFilter;
import ru.pavbatol.myplace.dto.annotation.CustomDateTimeFormat;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewSearchFilter extends AbstractSearchFilter<ViewSearchFilter> {
    List<String> uris;

    @Override
    public ViewSearchFilter populateNullFields() {
        ensureNonNullFields();
        setUris(getUris() != null ? getUris() : List.of());
        return this;
    }

    @Override
    public String toQuery(DateTimeFormatter formatter) {
        String query = super.toQuery(formatter);
        String addedQuery = getUris() == null ? "" : "uris=" + getUris().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return Stream.of(query, addedQuery)
                .filter(str -> !str.isEmpty())
                .collect(Collectors.joining("&"));
    }
}
