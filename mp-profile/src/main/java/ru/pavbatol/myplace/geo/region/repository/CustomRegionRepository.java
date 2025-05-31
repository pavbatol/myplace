package ru.pavbatol.myplace.geo.region.repository;

import org.springframework.data.domain.Slice;
import ru.pavbatol.myplace.geo.region.model.Region;

public interface CustomRegionRepository {
    Slice<Region> findPageByNamePrefixIgnoreCase(String nameStartWith, String lastSeenName, String lastSeenCountryName, int size);
}
