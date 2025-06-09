package ru.pavbatol.myplace.geo.city.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pavbatol.myplace.geo.common.pagination.Sliced;
import ru.pavbatol.myplace.app.util.Checker;
import ru.pavbatol.myplace.geo.city.dto.CityDto;
import ru.pavbatol.myplace.geo.city.mapper.CityMapper;
import ru.pavbatol.myplace.geo.city.model.City;
import ru.pavbatol.myplace.geo.city.repository.CityRepository;
import ru.pavbatol.myplace.geo.district.mapper.DistrictMapper;
import ru.pavbatol.myplace.geo.district.model.District;
import ru.pavbatol.myplace.geo.district.repository.DistrictRepository;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private static final String ENTITY_SIMPLE_NAME = City.class.getSimpleName();
    private final CityRepository repository;
    private final DistrictRepository districtRepository;
    private final CityMapper mapper;
    private final DistrictMapper districtMapper;

    @Transactional
    @Override
    public CityDto create(CityDto dto) {
        City entity = mapper.toEntity(dto, districtRepository, districtMapper);
        City saved = repository.save(entity);
        log.debug("Saved {}: {}", ENTITY_SIMPLE_NAME, saved);

        return mapper.toCityDto(saved);
    }

    @Transactional
    @Override
    public CityDto update(Long cityId, CityDto dto) {
        City original = Checker.getNonNullObject(repository, cityId);

        if (dto.getDistrict() != null) {
            if (dto.getDistrict().getId() == null) {
                throw new IllegalArgumentException("District.id in CityDto cannot be null when District provided on updating a City");
            }
            if (!original.getDistrict().getId().equals(dto.getDistrict().getId())) {
                District district = Checker.getNonNullObject(districtRepository, dto.getDistrict().getId());
                original.setDistrict(district);
            }
        }

        City updated = mapper.updateEntity(original, dto);
        updated = repository.save(updated);
        log.debug("Updated {}: {}", ENTITY_SIMPLE_NAME, updated);

        return mapper.toCityDto(updated);
    }

    @Transactional
    @Override
    public void delete(Long cityId) {
        repository.deleteById(cityId);
        log.debug("Deleted {}: with cityId: #{}", ENTITY_SIMPLE_NAME, cityId);
    }

    @Transactional(readOnly = true)
    @Override
    public CityDto getById(Long cityId) {
        City found = Checker.getNonNullObject(repository, cityId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);

        return mapper.toCityDto(found);
    }

    @Transactional(readOnly = true)
    @Override
    public SimpleSlice<CityDto> getAll(String nameStartWith, String lastSeenName, Long lastSeenId, int size) {
        log.debug("Finding {}(e)s with nameStartWith: {}, lastSeenName: {}, lastSeenId: {}, size: {}",
                ENTITY_SIMPLE_NAME, nameStartWith, lastSeenName, lastSeenId, size);

        Slice<City> slice = repository.findPageByNamePrefixIgnoreCase(nameStartWith, lastSeenName, lastSeenId, size);
        log.debug("Found {} {}(e)s, hasNext: {}", slice.getNumberOfElements(), ENTITY_SIMPLE_NAME, slice.hasNext());

        return Sliced.from(slice, mapper::toCityDto);
    }
}
