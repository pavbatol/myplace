package ru.pavbatol.myplace.geo.district.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pavbatol.myplace.geo.district.model.District;

public interface DistrictRepository extends CrudRepository<District, Long> {
}
