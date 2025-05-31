package ru.pavbatol.myplace.geo.street.service;

import ru.pavbatol.myplace.geo.street.dto.StreetDto;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;

public interface StreetService {
    StreetDto create(StreetDto dto);

    StreetDto update(Long streetId, StreetDto dto);

    void delete(Long streetId);

    StreetDto getById(Long streetId);

    SimpleSlice<StreetDto> getAll(String nameStartWith, String lastSeenName, Long lastSeenId, int size);
}
