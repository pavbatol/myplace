package ru.pavbatol.myplace.geo.country.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pavbatol.myplace.geo.country.model.Country;

public interface CountryRepository extends CrudRepository<Country, Long> {
}
