package ru.pavbatol.myplace.dto.view;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.pavbatol.myplace.dto.AbstractSearchFilter;

import javax.validation.constraints.Min;
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
    private static final int PAGE_NUMBER = 1;

    List<String> uris;

    @Min(1)
    Integer pageNumber;

    @Override
    public ViewSearchFilter setNullFieldsToDefault() {
        setBaseNullFieldsToDefault();
        setUris(getUris() != null ? getUris() : List.of());
        setPageNumber(getPageNumber() != null ? getPageNumber() : PAGE_NUMBER);
        return this;
    }

    @Override
    public String toQuery(DateTimeFormatter formatter) {
        return joinQueryParams(
                baseFilterToQuery(formatter),
                toParamFromStrings("uris", getUris()),
                toParam("pageNumber", String.valueOf(getPageNumber()))
        );
    }
}
