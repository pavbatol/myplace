package ru.pavbatol.myplace.dto.cart;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.pavbatol.myplace.dto.SortDirection;
import ru.pavbatol.myplace.dto.annotation.CustomDateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemSearchFilter {
    @CustomDateTimeFormat
    LocalDateTime start;

    @CustomDateTimeFormat
    LocalDateTime end;

    List<Long> itemIds;

    Boolean unique;

    SortDirection sortDirection;

    public CartItemSearchFilter setSortDirection(String name) {
        if (name != null) {
            this.sortDirection = SortDirection.valueOf(name.toUpperCase());
        } else {
            this.sortDirection = null;
        }
        return this;
    }
}
