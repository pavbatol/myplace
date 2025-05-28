package ru.pavbatol.myplace.geo.city.repository;

import org.springframework.beans.factory.annotation.Autowired;
import ru.pavbatol.myplace.app.pagination.AbstractGeoEntityPagingRepository;
import ru.pavbatol.myplace.geo.city.model.City;

import javax.persistence.EntityManager;

public class CustomCityRepositoryImpl extends AbstractGeoEntityPagingRepository<City> implements CustomCityRepository {

    @Autowired
    public CustomCityRepositoryImpl(EntityManager em) {
        super("district.region.country", City.class, em);
    }
}
