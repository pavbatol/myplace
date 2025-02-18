package ru.pavbatol.myplace.geo.house.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.pavbatol.myplace.geo.house.model.House;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {
    Slice<House> findByNumberStartingWithIgnoreCase(String numberStartWith, Pageable pageable);

    List<House> findByNumberInIgnoreCaseAndStreetIdIn(Iterable<String> houseNumbersLowercase, Iterable<Long> streetIds);
}
