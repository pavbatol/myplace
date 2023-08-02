package ru.pavbatol.myplace.dto.view;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.pavbatol.myplace.dto.AbstractSearchFilter;

import java.time.format.DateTimeFormatter;
import java.util.List;

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
        ensureNonNullBaseFields();
        setUris(getUris() != null ? getUris() : List.of());
        return this;
    }

    @Override
    public String toQuery(DateTimeFormatter formatter) {
        String baseQuery = baseFilterToQuery(formatter);
        String addedQuery = toParamFromStrings("uris", getUris());
        return joinQueryParams(baseQuery, addedQuery);
    }
}
