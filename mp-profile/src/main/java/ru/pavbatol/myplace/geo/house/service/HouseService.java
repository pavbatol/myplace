package ru.pavbatol.myplace.geo.house.service;

import org.springframework.data.domain.Slice;
import ru.pavbatol.myplace.geo.house.dto.HouseDto;

public interface HouseService {
    HouseDto create(HouseDto dto);

    HouseDto update(Long houseId, HouseDto dto);

    void delete(Long houseId);

    HouseDto getById(Long houseId);

    Slice<HouseDto> getAll(String numberStartWith, int page, int size);
}
