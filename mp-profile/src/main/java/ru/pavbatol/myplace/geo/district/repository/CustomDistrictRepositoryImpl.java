package ru.pavbatol.myplace.geo.district.repository;

import ru.pavbatol.myplace.geo.common.pagination.AbstractGeoEntityPagingRepository;
import ru.pavbatol.myplace.geo.district.model.District;

import javax.persistence.EntityManager;

public class CustomDistrictRepositoryImpl extends AbstractGeoEntityPagingRepository<District> implements CustomDistrictRepository {

    public CustomDistrictRepositoryImpl(EntityManager em) {
        super("region.country", District.class);
    }
}
