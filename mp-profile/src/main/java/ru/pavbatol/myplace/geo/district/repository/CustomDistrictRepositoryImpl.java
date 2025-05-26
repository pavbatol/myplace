package ru.pavbatol.myplace.geo.district.repository;

import org.springframework.beans.factory.annotation.Autowired;
import ru.pavbatol.myplace.app.pagination.AbstractGeoEntityPagingRepository;
import ru.pavbatol.myplace.geo.district.model.District;

import javax.persistence.EntityManager;

public class CustomDistrictRepositoryImpl extends AbstractGeoEntityPagingRepository<District> implements CustomDistrictRepository {

    @Autowired
    public CustomDistrictRepositoryImpl(EntityManager em) {
        super(District.class, "region", em);
    }
}
