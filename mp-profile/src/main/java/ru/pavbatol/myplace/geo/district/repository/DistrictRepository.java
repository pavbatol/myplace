package ru.pavbatol.myplace.geo.district.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pavbatol.myplace.geo.district.model.District;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long>, CustomDistrictRepository {
    List<District> findByNameInIgnoreCaseAndRegionIdIn(Iterable<String> districtNamesLowercase, Iterable<Long> regionIds);
}
