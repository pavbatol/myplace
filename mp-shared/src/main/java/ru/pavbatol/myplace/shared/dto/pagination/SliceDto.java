package ru.pavbatol.myplace.shared.dto.pagination;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

import java.util.List;

@Value
public class SliceDto<T> implements SimpleSlice<T> {
    List<T> content;
    int size;
    int numberOfElements;
    @Getter(AccessLevel.NONE)
    boolean hasNext;

    @Override
    public boolean hasNext() {
        return this.hasNext;
    }
}
