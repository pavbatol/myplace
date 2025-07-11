package ru.pavbatol.myplace.shared.dto.pagination;

public interface SimplePage<T> extends SimpleSlice<T> {
    int getNumber();

    long getTotalElements();

    int getTotalPages();
}
