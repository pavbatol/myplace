package ru.pavbatol.myplace.geo.house.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pavbatol.myplace.app.Util.Checker;
import ru.pavbatol.myplace.geo.house.dto.HouseDto;
import ru.pavbatol.myplace.geo.house.mapper.HouseMapper;
import ru.pavbatol.myplace.geo.house.model.House;
import ru.pavbatol.myplace.geo.house.repository.HouseRepository;
import ru.pavbatol.myplace.geo.street.mapper.StreetMapper;
import ru.pavbatol.myplace.geo.street.repository.StreetRepository;

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
    public Slice<HouseDto> getAll(String numberStartWith, int page, int size) {
        log.debug("Finding {}(e)s with numberStartWith: {}, page: {}, size: {}", ENTITY_SIMPLE_NAME, numberStartWith, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "number"));
        Slice<House> found;
        if (numberStartWith != null && !numberStartWith.isBlank()) {
            found = repository.findByNumberStartingWithIgnoreCase(numberStartWith, pageable);
        } else {
            found = repository.findAll(pageable);
        }
        log.debug("Found Slice of {}: {}, numberOfElements: {}", ENTITY_SIMPLE_NAME, found, found.getNumberOfElements());

        return found.map(mapper::toHouseDto);
    }
}
