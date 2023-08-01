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
public class CartItemSearchFilter extends AbstractSearchFilter<CartItemSearchFilter> {
    List<Long> itemIds;

    @Override
    public CartItemSearchFilter populateNullFields() {
        ensureNonNullFields();
        setItemIds(getItemIds() != null ? getItemIds() : List.of());
        return this;
    }

    @Override
    public String toQuery(DateTimeFormatter formatter) {
        String query = super.toQuery(formatter);
        String addedQuery = getItemIds() == null ? "" : "itemIds=" + getItemIds().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return Stream.of(query, addedQuery)
                .filter(str -> !str.isEmpty())
                .collect(Collectors.joining("&"));
    }
}
