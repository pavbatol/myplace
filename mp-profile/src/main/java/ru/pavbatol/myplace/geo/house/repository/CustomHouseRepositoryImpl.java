package ru.pavbatol.myplace.geo.house.repository;

import ru.pavbatol.myplace.geo.common.pagination.AbstractGeoEntityPagingRepository;
import ru.pavbatol.myplace.geo.house.model.House;

import javax.persistence.EntityManager;

public class CustomHouseRepositoryImpl extends AbstractGeoEntityPagingRepository<House> implements CustomHouseRepository {

    public CustomHouseRepositoryImpl(EntityManager em) {
        super("number", "street.city.district.region.country", House.class);
    }
}
