package ru.pavbatol.myplace.geo.region.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pavbatol.myplace.app.util.Checker;
import ru.pavbatol.myplace.geo.common.pagination.Sliced;
import ru.pavbatol.myplace.geo.country.mapper.CountryMapper;
import ru.pavbatol.myplace.geo.country.model.Country;
import ru.pavbatol.myplace.geo.country.repository.CountryRepository;
import ru.pavbatol.myplace.geo.region.mapper.RegionMapper;
import ru.pavbatol.myplace.geo.region.model.Region;
import ru.pavbatol.myplace.geo.region.repository.RegionRepository;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.region.RegionDto;

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

    @Transactional
    @Override
    public RegionDto update(Long regionId, RegionDto dto) {
        Region original = Checker.getNonNullObject(repository, regionId);

        if (dto.getCountry() != null) {
            if (dto.getCountry().getId() == null) {
                throw new IllegalArgumentException("Country.id in RegionDto cannot be null when Country provided on updating a Region");
            }
            if (!original.getCountry().getId().equals(dto.getCountry().getId())) {
                Country country = Checker.getNonNullObject(countryRepository, dto.getCountry().getId());
                original.setCountry(country);
            }
        }

        Region updated = mapper.updateEntity(original, dto);
        updated = repository.save(updated);
        log.debug("Updated {}: {}", ENTITY_SIMPLE_NAME, updated);

        return mapper.toRegionDto(updated);
    }

    @Transactional
    @Override
    public void delete(Long regionId) {
        repository.deleteById(regionId);
        log.debug("Deleted {}: with regionId: #{}", ENTITY_SIMPLE_NAME, regionId);
    }

    @Transactional(readOnly = true)
    @Override
    public RegionDto getById(Long regionId) {
        Region found = Checker.getNonNullObject(repository, regionId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);

        return mapper.toRegionDto(found);
    }

    @Transactional(readOnly = true)
    @Override
    public SimpleSlice<RegionDto> getAll(String nameStartWith, String lastSeenName, String lastSeenCountryName, int size) {
        log.debug("Finding {}(e)s with nameStartWith: {}, lastSeenName: {}, lastSeenCountryName: {}, size: {}",
                ENTITY_SIMPLE_NAME, nameStartWith, lastSeenName, lastSeenCountryName, size);

        Slice<Region> slice = repository.findPageByNamePrefixIgnoreCase(nameStartWith, lastSeenName, lastSeenCountryName, size);
        log.debug("Found {} {}(e)s, hasNext: {}", slice.getNumberOfElements(), ENTITY_SIMPLE_NAME, slice.hasNext());

        return Sliced.from(slice, mapper::toRegionDto);
    }
}
