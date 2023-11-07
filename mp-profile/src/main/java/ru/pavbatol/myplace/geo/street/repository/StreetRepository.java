package ru.pavbatol.myplace.geo.street.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.pavbatol.myplace.geo.street.model.Street;

public interface StreetRepository extends JpaRepository<Street, Long> {
    Slice<Street> findByNameStartingWith(String nameStartWith, Pageable pageable);
}
