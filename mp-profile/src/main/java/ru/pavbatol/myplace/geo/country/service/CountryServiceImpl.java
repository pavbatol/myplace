package ru.pavbatol.myplace.geo.country.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pavbatol.myplace.geo.common.pagination.Sliced;
import ru.pavbatol.myplace.app.util.Checker;
import ru.pavbatol.myplace.geo.country.dto.CountryDto;
import ru.pavbatol.myplace.geo.country.mapper.CountryMapper;
import ru.pavbatol.myplace.geo.country.model.Country;
import ru.pavbatol.myplace.geo.country.repository.CountryRepository;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private static final String ENTITY_SIMPLE_NAME = Country.class.getSimpleName();
    private final CountryRepository repository;
    private final CountryMapper mapper;

    @Transactional
    @Override
    public CountryDto create(CountryDto dto) {
        Country entity = mapper.toEntity(dto);
        Country saved = repository.save(entity);
        log.debug("Saved {}: {}", ENTITY_SIMPLE_NAME, saved);

        return mapper.toCountryDto(saved);
    }

    @Transactional
    @Override
    public CountryDto update(Long countryId, CountryDto dto) {
        Country original = Checker.getNonNullObject(repository, countryId);
        Country updated = mapper.updateEntity(original, dto);
        updated = repository.save(updated);
        log.debug("Updated {}: {}", ENTITY_SIMPLE_NAME, updated);

        return mapper.toCountryDto(updated);
    }

    @Transactional
    @Override
    public void delete(Long countryId) {
        repository.deleteById(countryId);
        log.debug("Deleted {}: with countryId: #{}", ENTITY_SIMPLE_NAME, countryId);
    }

    @Transactional(readOnly = true)
    @Override
    public CountryDto getById(Long countryId) {
        Country found = Checker.getNonNullObject(repository, countryId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);

        return mapper.toCountryDto(found);
    }

    @Transactional(readOnly = true)
    @Override
    public SimpleSlice<CountryDto> getAll(String nameStartWith, String lastSeenName, int size) {
        log.debug("Finding {}(s) with nameStartWith: {}, lastSeenName: {}, size: {}",
                ENTITY_SIMPLE_NAME, nameStartWith, lastSeenName, size);

        Slice<Country> slice = repository.findPageByNamePrefixIgnoreCase(nameStartWith, lastSeenName, size);
        log.debug("Found {} {}(s), hasNext: {}", slice.getNumberOfElements(), ENTITY_SIMPLE_NAME, slice.hasNext());

        return Sliced.from(slice, mapper::toCountryDto);
    }
}
