package ru.pavbatol.myplace.shared.pagination;

public interface SimplePage<T> extends SimpleSlice<T> {
    int getNumber();

    long getTotalElements();

    int getTotalPages();
}
