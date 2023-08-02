package ru.pavbatol.myplace.dto.shipping;

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
public class ShippingGeoSearchFilter extends AbstractSearchFilter<ShippingGeoSearchFilter> {
    List<Long> itemIds;
    List<String> countries;

    @Override
    public ShippingGeoSearchFilter populateNullFields() {
        ensureNonNullBaseFields();
        setItemIds(getItemIds() != null ? getItemIds() : List.of());
        setCountries(getItemIds() != null ? getCountries() : List.of());
        return this;
    }

    @Override
    public String toQuery(DateTimeFormatter formatter) {
        String baseQuery = baseFilterToQuery(formatter);
        String countriesQuery = toParamFromLongs("countries", getItemIds());
        String itemIdsQuery = toParamFromLongs("itemIds", getItemIds());
        return joinQueryParams(baseQuery, countriesQuery, itemIdsQuery);
    }
}
