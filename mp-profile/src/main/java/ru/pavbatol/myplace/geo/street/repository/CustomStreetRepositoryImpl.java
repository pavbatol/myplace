package ru.pavbatol.myplace.geo.street.repository;

import org.springframework.beans.factory.annotation.Autowired;
import ru.pavbatol.myplace.geo.common.pagination.AbstractGeoEntityPagingRepository;
import ru.pavbatol.myplace.geo.street.model.Street;

import javax.persistence.EntityManager;

public class CustomStreetRepositoryImpl extends AbstractGeoEntityPagingRepository<Street> implements CustomStreetRepository {

    @Autowired
    public CustomStreetRepositoryImpl(EntityManager em) {
        super("city.district.region.country", Street.class, em);
    }
}
