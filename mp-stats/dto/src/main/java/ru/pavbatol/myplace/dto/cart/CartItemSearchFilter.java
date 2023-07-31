package ru.pavbatol.myplace.dto.cart;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.pavbatol.myplace.dto.AbstractSearchFilter;

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

    @Override
    public CartItemSearchFilter populateNullFields() {
        ensureNonNullFields();
        setItemIds(getItemIds() != null ? getItemIds() : List.of());
        return this;
    }
}
