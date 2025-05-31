package ru.pavbatol.myplace.geo.region.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pavbatol.myplace.geo.region.model.Region;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long>, CustomRegionRepository {
    List<Region> findByNameInIgnoreCaseAndCountryIdIn(Iterable<String> regionNamesLowercase, Iterable<Long> countryIds);
}
