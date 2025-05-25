package ru.pavbatol.myplace.geo.district.repository;

import org.springframework.data.domain.Slice;
import ru.pavbatol.myplace.geo.district.model.District;

public interface CustomDistrictRepository {
    Slice<District> findPageByNamePrefixIgnoreCase(String nameStartWith, String lastSeenName, Long lastSeenId, int size);
}
