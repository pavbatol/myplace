package ru.pavbatol.myplace.geo.city.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.pavbatol.myplace.geo.city.model.City;

public interface CityRepository extends JpaRepository<City, Long> {
    Slice<City> findByNameStartingWithIgnoreCase(String nameStartWith, Pageable pageable);
}
