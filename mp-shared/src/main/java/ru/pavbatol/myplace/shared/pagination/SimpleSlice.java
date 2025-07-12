package ru.pavbatol.myplace.shared.pagination;

import java.util.List;

public interface SimpleSlice<T> {
    List<T> getContent();

    int getSize();

    int getNumberOfElements();

    boolean isHasNext();
}
