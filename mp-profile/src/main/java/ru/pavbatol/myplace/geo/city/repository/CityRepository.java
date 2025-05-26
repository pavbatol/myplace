package ru.pavbatol.myplace.geo.city.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pavbatol.myplace.geo.city.model.City;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long>, CustomCityRepository {
    List<City> findByNameInIgnoreCaseAndDistrictIdIn(Iterable<String> cityNamesLowercase, Iterable<Long> districtIds);
}
