package ru.pavbatol.myplace.geo.country.repository;

import org.springframework.data.domain.Slice;
import ru.pavbatol.myplace.geo.country.model.Country;

public interface CustomCountryRepository {
    Slice<Country> findNextPage(String lastSeenName, int size);

    Slice<Country> findNextPageByNamePrefixIgnoreCase(String nameStartWith, String lastSeenName, int size);
}
