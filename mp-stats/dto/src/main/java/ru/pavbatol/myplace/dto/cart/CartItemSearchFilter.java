package ru.pavbatol.myplace.dto.cart;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.pavbatol.myplace.dto.SortDirection;
import ru.pavbatol.myplace.dto.annotation.CustomDateTimeFormat;

import java.rmi.server.UID;
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
}
