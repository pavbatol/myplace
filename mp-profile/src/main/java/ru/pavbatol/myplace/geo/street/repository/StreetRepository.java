package ru.pavbatol.myplace.geo.street.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pavbatol.myplace.geo.street.model.Street;

public interface StreetRepository extends CrudRepository<Street, Long> {
}
