package ru.pavbatol.myplace.geo.street.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pavbatol.myplace.geo.city.model.City;
import ru.pavbatol.myplace.geo.common.pagination.Sliced;
import ru.pavbatol.myplace.app.util.Checker;
import ru.pavbatol.myplace.geo.city.mapper.CityMapper;
import ru.pavbatol.myplace.geo.city.repository.CityRepository;
import ru.pavbatol.myplace.geo.street.dto.StreetDto;
import ru.pavbatol.myplace.geo.street.mapper.StreetMapper;
import ru.pavbatol.myplace.geo.street.model.Street;
import ru.pavbatol.myplace.geo.street.repository.StreetRepository;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;

@Slf4j
@Service
@RequiredArgsConstructor
public class StreetServiceImpl implements StreetService {
    private static final String ENTITY_SIMPLE_NAME = Street.class.getSimpleName();
    private final StreetRepository repository;
    private final CityRepository cityRepository;
    private final StreetMapper mapper;
    private final CityMapper cityMapper;

    @Transactional
    @Override
    public StreetDto create(StreetDto dto) {
        Street entity = mapper.toEntity(dto, cityRepository, cityMapper);
        Street saved = repository.save(entity);
        log.debug("Saved {}: {}", ENTITY_SIMPLE_NAME, saved);

        return mapper.toStreetDto(saved);
    }

    @Transactional
    @Override
    public StreetDto update(Long streetId, StreetDto dto) {
        Street original = Checker.getNonNullObject(repository, streetId);

        if (dto.getCity() != null) {
            if (dto.getCity().getId() == null) {
                throw new IllegalArgumentException("City.id in StreetDto cannot be null when City provided on updating a Street");
            }
            if (!original.getCity().getId().equals(dto.getCity().getId())) {
                City city = Checker.getNonNullObject(cityRepository, dto.getCity().getId());
                original.setCity(city);
            }
        }

        Street updated = mapper.updateEntity(original, dto);
        updated = repository.save(updated);
        log.debug("Updated {}: {}", ENTITY_SIMPLE_NAME, updated);

        return mapper.toStreetDto(updated);
    }

    @Transactional
    @Override
    public void delete(Long streetId) {
        repository.deleteById(streetId);
        log.debug("Deleted {}: with streetId: #{}", ENTITY_SIMPLE_NAME, streetId);
    }

    @Transactional(readOnly = true)
    @Override
    public StreetDto getById(Long streetId) {
        Street found = Checker.getNonNullObject(repository, streetId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);

        return mapper.toStreetDto(found);
    }

    @Transactional(readOnly = true)
    @Override
    public SimpleSlice<StreetDto> getAll(String nameStartWith, String lastSeenName, Long lastSeenId, int size) {
        log.debug("Finding {}(e)s with nameStartWith: {}, lastSeenName: {}, lastSeenId: {}, size: {}",
                ENTITY_SIMPLE_NAME, nameStartWith, lastSeenName, lastSeenId, size);

        Slice<Street> slice = repository.findPageByNamePrefixIgnoreCase(nameStartWith, lastSeenName, lastSeenId, size);
        log.debug("Found {} {}(e)s, hasNext: {}", slice.getNumberOfElements(), ENTITY_SIMPLE_NAME, slice.hasNext());

        return Sliced.from(slice, mapper::toStreetDto);
    }
}
