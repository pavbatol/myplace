package ru.pavbatol.myplace.geo.district.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.pavbatol.myplace.geo.district.model.District;

public interface DistrictRepository extends JpaRepository<District, Long> {
    Slice<District> findByNameStartingWithIgnoreCase(String nameStartWith, Pageable pageable);
}
