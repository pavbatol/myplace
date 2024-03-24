package ru.pavbatol.myplace.geo.country.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.app.Util.Checker;
import ru.pavbatol.myplace.geo.country.dto.CountryDto;
import ru.pavbatol.myplace.geo.country.mapper.CountryMapper;
import ru.pavbatol.myplace.geo.country.model.Country;
import ru.pavbatol.myplace.geo.country.repository.CountryRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private static final String ENTITY_SIMPLE_NAME = Country.class.getSimpleName();
    private final CountryRepository repository;
    private final CountryMapper mapper;

    @Override
    public CountryDto create(CountryDto dto) {
        Country entity = mapper.toEntity(dto);
        Country saved = repository.save(entity);
        log.debug("Saved {}: {}", ENTITY_SIMPLE_NAME, saved);

        return mapper.toCountryDto(saved);
    }

    @Override
    public CountryDto update(Long countryId, CountryDto dto) {
        Country original = Checker.getNonNullObject(repository, countryId);
        Country updated = mapper.updateEntity(original, dto);
        updated = repository.save(updated);
        log.debug("Updated {}: {}", ENTITY_SIMPLE_NAME, updated);

        return mapper.toCountryDto(updated);
    }

    @Override
    public void delete(Long countryId) {
        repository.deleteById(countryId);
        log.debug("Deleted {}: with countryId: #{}", ENTITY_SIMPLE_NAME, countryId);
    }

    @Override
    public CountryDto getById(Long countryId) {
        Country found = Checker.getNonNullObject(repository, countryId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);

        return mapper.toCountryDto(found);
    }

    @Override
    public Slice<CountryDto> getAll(String nameStartWith, int page, int size) {
        log.debug("Finding {}(e)s with nameStartWith: {}, page: {}, size: {}", ENTITY_SIMPLE_NAME, nameStartWith, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Slice<Country> found;
        if (nameStartWith != null && !nameStartWith.isBlank()) {
            found = repository.findByNameStartingWithIgnoreCase(nameStartWith, pageable);
        } else {
            found = repository.findAll(pageable);
        }
        log.debug("Found Slice of {}: {}, numberOfElements: {}", ENTITY_SIMPLE_NAME, found, found.getNumberOfElements());

        return found.map(mapper::toCountryDto);
    }
}
