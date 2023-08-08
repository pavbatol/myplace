package ru.pavbatol.myplace.dto.cart;

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
public class CartItemSearchFilter extends AbstractSearchFilter<CartItemSearchFilter> {
    List<Long> itemIds;
    Long lastItemId;
    Integer lastCartItemCount;

    @Override
    public CartItemSearchFilter setNullFieldsToDefault() {
        setBaseNullFieldsToDefault();
        setItemIds(getItemIds() != null ? getItemIds() : List.of());
        setLastItemId(getLastItemId() != null ? getLastItemId() : null);
        setLastCartItemCount(getLastCartItemCount() != null ? getLastCartItemCount() : null);
        return this;
    }

    @Override
    public String toQuery(DateTimeFormatter formatter) {
        return joinQueryParams(
                baseFilterToQuery(formatter),
                toParamFromLongs("itemIds", getItemIds()),
                toParam("lastItemId", String.valueOf(getLastItemId())),
                toParam("lastCartItemCount", String.valueOf(getLastCartItemCount()))
        );
    }
}
