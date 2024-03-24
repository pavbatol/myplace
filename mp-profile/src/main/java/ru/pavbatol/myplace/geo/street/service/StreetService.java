package ru.pavbatol.myplace.geo.street.service;

import org.springframework.data.domain.Slice;
import ru.pavbatol.myplace.geo.street.dto.StreetDto;

public interface StreetService {
    StreetDto create(StreetDto dto);

    StreetDto update(Long streetId, StreetDto dto);

    void delete(Long streetId);

    StreetDto getById(Long streetId);

    Slice<StreetDto> getAll(String nameStartWith, int page, int size);
}
