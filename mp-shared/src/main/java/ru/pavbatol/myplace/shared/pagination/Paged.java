package ru.pavbatol.myplace.shared.pagination;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Paged<T> implements SimplePage<T> {
    List<T> content;

    int size;

    int numberOfElements;

    int number;

    long totalElements;

    int totalPages;

    boolean hasNext;
}
