package ru.pavbatol.myplace.dto.view;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class ViewSearchFilter {
    @CustomDateTimeFormat
    LocalDateTime start;

    @CustomDateTimeFormat
    LocalDateTime end;

    List<String> uris;

    Boolean unique;

    SortDirection sortDirection;

    public ViewSearchFilter setSortDirection(String name) {
        if (name != null) {
            this.sortDirection = SortDirection.valueOf(name.toUpperCase());
        } else {
            this.sortDirection = null;
        }
        return this;
    }
}
