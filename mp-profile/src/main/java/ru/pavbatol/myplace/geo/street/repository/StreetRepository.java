package ru.pavbatol.myplace.geo.street.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.pavbatol.myplace.geo.street.model.Street;

import java.util.List;

public interface StreetRepository extends JpaRepository<Street, Long>, CustomStreetRepository {
    Slice<Street> findByNameStartingWithIgnoreCase(String nameStartWith, Pageable pageable);

    List<Street> findByNameInIgnoreCaseAndCityIdIn(Iterable<String> streetNamesLowercase, Iterable<Long> cityIds);
}
