package ru.pavbatol.myplace.geo.region.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pavbatol.myplace.app.Util.Checker;
import ru.pavbatol.myplace.geo.country.mapper.CountryMapper;
import ru.pavbatol.myplace.geo.country.repository.CountryRepository;
import ru.pavbatol.myplace.geo.region.dto.RegionDto;
import ru.pavbatol.myplace.geo.region.mapper.RegionMapper;
import ru.pavbatol.myplace.geo.region.model.Region;
import ru.pavbatol.myplace.geo.region.repository.RegionRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {
    private static final String ENTITY_SIMPLE_NAME = Region.class.getSimpleName();
    private final RegionRepository repository;
    private final CountryRepository countryRepository;
    private final RegionMapper mapper;
    private final CountryMapper countryMapper;

    @Transactional
    @Override
    public RegionDto create(RegionDto dto) {
        Region entity = mapper.toEntity(dto, countryRepository, countryMapper);
        Region saved = repository.save(entity);
        log.debug("Saved {}: {}", ENTITY_SIMPLE_NAME, saved);

        return mapper.toRegionDto(saved);
    }

    @Override
    public RegionDto update(Long regionId, RegionDto dto) {
        Region original = Checker.getNonNullObject(repository, regionId);
        Region updated = mapper.updateEntity(original, dto);
        updated = repository.save(updated);
        log.debug("Updated {}: {}", ENTITY_SIMPLE_NAME, updated);

        return mapper.toRegionDto(updated);
    }

    @Override
    public void delete(Long regionId) {
        repository.deleteById(regionId);
        log.debug("Deleted {}: with regionId: #{}", ENTITY_SIMPLE_NAME, regionId);
    }

    @Override
    public RegionDto getById(Long regionId) {
        Region found = Checker.getNonNullObject(repository, regionId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);

        return mapper.toRegionDto(found);
    }

    @Override
    public Slice<RegionDto> getAll(String nameStartWith, int page, int size) {
        log.debug("Finding {}(e)s with nameStartWith: {}, page: {}, size: {}", ENTITY_SIMPLE_NAME, nameStartWith, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Slice<Region> found;
        if (nameStartWith != null && !nameStartWith.isBlank()) {
            found = repository.findByNameStartingWithIgnoreCase(nameStartWith, pageable);
        } else {
            found = repository.findAll(pageable);
        }
        log.debug("Found Slice of {}: {}, numberOfElements: {}", ENTITY_SIMPLE_NAME, found, found.getNumberOfElements());

        return found.map(mapper::toRegionDto);
    }
}
