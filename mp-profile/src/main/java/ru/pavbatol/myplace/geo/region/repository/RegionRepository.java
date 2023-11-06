package ru.pavbatol.myplace.geo.region.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pavbatol.myplace.geo.region.model.Region;

public interface RegionRepository extends CrudRepository<Region, Long> {
}
