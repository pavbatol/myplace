package ru.pavbatol.myplace.geo.house.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pavbatol.myplace.geo.house.model.House;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long>, CustomHouseRepository {
    List<House> findByNumberInIgnoreCaseAndStreetIdIn(Iterable<String> houseNumbersLowercase, Iterable<Long> streetIds);
}
