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
    Integer lastCityCount;
    Integer lastCountryCount;
    Long lastItemId;

    @Override
    public ShippingGeoSearchFilter setNullFieldsToDefault() {
        setBaseNullFieldsToDefault();
        setItemIds(getItemIds() != null ? getItemIds() : List.of());
        setCountries(getItemIds() != null ? getCountries() : List.of());
        setLastCityCount(getLastCityCount() != null ? getLastCityCount() : null);
        setLastCountryCount(getLastCountryCount() != null ? getLastCountryCount() : null);
        setLastItemId(getLastItemId() != null ? getLastItemId() : null);
        return this;
    }

    @Override
    public String toQuery(DateTimeFormatter formatter) {
        return joinQueryParams(
                baseFilterToQuery(formatter),
                toParamFromLongs("itemIds", getItemIds()),
                toParamFromStrings("countries", getCountries()),
                toParam("lastCityCount", String.valueOf(getLastCityCount())),
                toParam("lastCountryCount", String.valueOf(getLastCountryCount())),
                toParam("lastItemId", String.valueOf(getLastItemId()))
        );
    }
}
