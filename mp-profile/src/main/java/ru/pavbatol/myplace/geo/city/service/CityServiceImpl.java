package ru.pavbatol.myplace.geo.city.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.app.Util.Checker;
import ru.pavbatol.myplace.geo.city.dto.CityDto;
import ru.pavbatol.myplace.geo.city.mapper.CityMapper;
import ru.pavbatol.myplace.geo.city.model.City;
import ru.pavbatol.myplace.geo.city.repository.CityRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private static final String ENTITY_SIMPLE_NAME = City.class.getSimpleName();
    private final CityRepository repository;
    private final CityMapper mapper;

    @Override
    public CityDto create(CityDto dto) {
        City entity = mapper.toEntity(dto);
        City saved = repository.save(entity);
        log.debug("Saved {}: {}", ENTITY_SIMPLE_NAME, saved);

        return mapper.toCityDto(saved);
    }

    @Override
    public CityDto update(Long cityId, CityDto dto) {
        City original = Checker.getNonNullObject(repository, cityId);
        City updated = mapper.updateEntity(original, dto);
        updated = repository.save(updated);
        log.debug("Updated {}: {}", ENTITY_SIMPLE_NAME, updated);

        return mapper.toCityDto(updated);
    }

    @Override
    public void delete(Long cityId) {
        repository.deleteById(cityId);
        log.debug("Deleted {}: with cityId: #{}", ENTITY_SIMPLE_NAME, cityId);
    }

    @Override
    public CityDto getById(Long cityId) {
        City found = Checker.getNonNullObject(repository, cityId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);

        return mapper.toCityDto(found);
    }

    @Override
    public Slice<CityDto> getAll(String nameStartWith, int page, int size) {
        log.debug("Finding {}(e)s with nameStartWith: {}, page: {}, size: {}", ENTITY_SIMPLE_NAME, nameStartWith, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Slice<City> found;
        if (nameStartWith != null && !nameStartWith.isBlank()) {
            found = repository.findByNameStartingWith(nameStartWith, pageable);
        } else {
            found = repository.findAll(pageable);
        }
        log.debug("Found Slice of {}: {}, numberOfElements: {}", ENTITY_SIMPLE_NAME, found, found.getNumberOfElements());

        return found.map(mapper::toCityDto);
    }
}
