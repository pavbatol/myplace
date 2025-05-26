package ru.pavbatol.myplace.app.pagination;

import org.springframework.data.domain.Slice;

public interface GeoEntityPagingRepository<T> {
    Slice<T> findPageByNamePrefixIgnoreCase(String nameStartWith, String lastSeenName, Long lastSeenId, int size);
}
