package ru.pavbatol.myplace.geo.region.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.pavbatol.myplace.geo.region.model.Region;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Slice<Region> findByNameStartingWithIgnoreCase(String nameStartWith, Pageable pageable);

    List<Region> findByNameInIgnoreCaseAndCountryIdIn(Iterable<String> regionNamesLowercase, Iterable<Long> countryIds);
}
