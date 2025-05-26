package ru.pavbatol.myplace.geo.city.repository;

import org.springframework.data.domain.Slice;
import ru.pavbatol.myplace.geo.city.model.City;

public interface CustomCityRepository {
    Slice<City> findPageByNamePrefixIgnoreCase(String nameStartWith, String lastSeenName, Long lastSeenId, int size);
}
