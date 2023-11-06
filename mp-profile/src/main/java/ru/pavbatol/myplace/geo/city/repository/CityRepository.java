package ru.pavbatol.myplace.geo.city.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pavbatol.myplace.geo.city.model.City;

public interface CityRepository extends CrudRepository<City, Long> {
}
