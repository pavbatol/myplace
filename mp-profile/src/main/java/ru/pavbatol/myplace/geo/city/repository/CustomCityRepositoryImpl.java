package ru.pavbatol.myplace.geo.city.repository;

import ru.pavbatol.myplace.geo.common.pagination.AbstractGeoEntityPagingRepository;
import ru.pavbatol.myplace.geo.city.model.City;

import javax.persistence.EntityManager;

public class CustomCityRepositoryImpl extends AbstractGeoEntityPagingRepository<City> implements CustomCityRepository {

    public CustomCityRepositoryImpl(EntityManager em) {
        super("district.region.country", City.class);
    }
}
