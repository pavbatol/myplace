package ru.pavbatol.myplace.geo.house.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.pavbatol.myplace.geo.house.model.House;

public interface HouseRepository extends JpaRepository<House, Long> {
    Slice<House> findByNumberStartingWith(String numberStartWith, Pageable pageable);
}
