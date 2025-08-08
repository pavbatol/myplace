package ru.pavbatol.myplace.shared.pagination;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Sliced<T> implements SimpleSlice<T> {
    List<T> content;

    int size;

    int numberOfElements;

    boolean hasNext;
}
