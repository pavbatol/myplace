package ru.pavbatol.myplace.geo.country.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.pavbatol.myplace.geo.country.model.Country;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Slice<Country> findByNameStartingWithIgnoreCase(String nameStartWith, Pageable pageable);

    List<Country> findByNameInIgnoreCase(List<String> collect);
}
