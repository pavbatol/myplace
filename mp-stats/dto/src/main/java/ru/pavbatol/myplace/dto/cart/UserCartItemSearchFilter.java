package ru.pavbatol.myplace.dto.cart;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.pavbatol.myplace.dto.AbstractSearchFilter;

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
public class UserCartItemSearchFilter extends AbstractSearchFilter<UserCartItemSearchFilter> {
    List<Long> userIds;

    @Override
    public UserCartItemSearchFilter populateNullFields() {
        ensureNonNullFields();
        setUserIds(getUserIds() != null ? getUserIds() : List.of());
        return this;
    }

    @Override
    public String toQuery(DateTimeFormatter formatter) {
        String query = super.toQuery(formatter);
        String addedQuery = getUserIds() == null ? "" : "userIds=" + getUserIds().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return Stream.of(query, addedQuery)
                .filter(str -> !str.isEmpty())
                .collect(Collectors.joining("&"));
    }
}
