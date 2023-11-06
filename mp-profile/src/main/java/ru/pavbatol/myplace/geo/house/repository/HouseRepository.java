package ru.pavbatol.myplace.geo.house.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pavbatol.myplace.geo.house.model.House;

public interface HouseRepository extends CrudRepository<House, Long> {
}
