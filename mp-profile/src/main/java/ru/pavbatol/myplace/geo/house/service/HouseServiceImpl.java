package ru.pavbatol.myplace.geo.house.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pavbatol.myplace.app.pagination.Sliced;
import ru.pavbatol.myplace.app.util.Checker;
import ru.pavbatol.myplace.geo.house.dto.HouseDto;
import ru.pavbatol.myplace.geo.house.mapper.HouseMapper;
import ru.pavbatol.myplace.geo.house.model.House;
import ru.pavbatol.myplace.geo.house.repository.HouseRepository;
import ru.pavbatol.myplace.geo.street.mapper.StreetMapper;
import ru.pavbatol.myplace.geo.street.repository.StreetRepository;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;

@Slf4j
@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {
    private static final String ENTITY_SIMPLE_NAME = House.class.getSimpleName();
    private final HouseRepository repository;
    private final StreetRepository streetRepository;
    private final HouseMapper mapper;
    private final StreetMapper streetMapper;

    @Transactional
    @Override
    public HouseDto create(HouseDto dto) {
        House entity = mapper.toEntity(dto, streetRepository, streetMapper);
        House saved = repository.save(entity);
        log.debug("Saved {}: {}", ENTITY_SIMPLE_NAME, saved);

        return mapper.toHouseDto(saved);
    }

    @Override
    public HouseDto update(Long houseId, HouseDto dto) {
        House original = Checker.getNonNullObject(repository, houseId);
        House updated = mapper.updateEntity(original, dto);
        updated = repository.save(updated);
        log.debug("Updated {}: {}", ENTITY_SIMPLE_NAME, updated);

        return mapper.toHouseDto(updated);
    }

    @Override
    public void delete(Long houseId) {
        repository.deleteById(houseId);
        log.debug("Deleted {}: with houseId: #{}", ENTITY_SIMPLE_NAME, houseId);
    }

    @Override
    public HouseDto getById(Long houseId) {
        House found = Checker.getNonNullObject(repository, houseId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);

        return mapper.toHouseDto(found);
    }

    @Override
    public SimpleSlice<HouseDto> getAll(String numberStartWith, String lastSeenNumber, Long lastSeenId, int size) {
        log.debug("Finding {}(e)s with numberStartWith: {}, lastSeenNumber: {}, lastSeenId: {}, size: {}",
                ENTITY_SIMPLE_NAME, numberStartWith, lastSeenNumber, lastSeenId, size);

        Slice<House> slice = repository.findPageByNamePrefixIgnoreCase(numberStartWith, lastSeenNumber, lastSeenId, size);
        log.debug("Found {} {}(e)s, hasNext: {}", slice.getSize(), ENTITY_SIMPLE_NAME, slice.hasNext());

        return Sliced.from(slice, mapper::toHouseDto);
    }
}
