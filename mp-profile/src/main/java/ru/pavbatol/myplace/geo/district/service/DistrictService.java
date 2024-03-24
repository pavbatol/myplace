package ru.pavbatol.myplace.geo.district.service;

import org.springframework.data.domain.Slice;
import ru.pavbatol.myplace.geo.district.dto.DistrictDto;

public interface DistrictService {
    DistrictDto create(DistrictDto dto);

    DistrictDto update(Long districtId, DistrictDto dto);

    void delete(Long districtId);

    DistrictDto getById(Long districtId);

    Slice<DistrictDto> getAll(String nameStartWith, int page, int size);
}
