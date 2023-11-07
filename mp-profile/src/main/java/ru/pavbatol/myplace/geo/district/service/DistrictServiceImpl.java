package ru.pavbatol.myplace.geo.district.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.app.Util.Checker;
import ru.pavbatol.myplace.geo.district.dto.DistrictDto;
import ru.pavbatol.myplace.geo.district.mapper.DistrictMapper;
import ru.pavbatol.myplace.geo.district.model.District;
import ru.pavbatol.myplace.geo.district.repository.DistrictRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {
    private static final String ENTITY_SIMPLE_NAME = District.class.getSimpleName();
    private final DistrictRepository repository;
    private final DistrictMapper mapper;

    @Override
    public DistrictDto create(DistrictDto dto) {
        District entity = mapper.toEntity(dto);
        District saved = repository.save(entity);
        log.debug("Saved {}: {}", ENTITY_SIMPLE_NAME, saved);

        return mapper.toDistrictDto(saved);
    }

    @Override
    public DistrictDto update(Long districtId, DistrictDto dto) {
        District original = Checker.getNonNullObject(repository, districtId);
        District updated = mapper.updateEntity(original, dto);
        updated = repository.save(updated);
        log.debug("Updated {}: {}", ENTITY_SIMPLE_NAME, updated);

        return mapper.toDistrictDto(updated);
    }

    @Override
    public void delete(Long districtId) {
        repository.deleteById(districtId);
        log.debug("Deleted {}: with cityId: #{}", ENTITY_SIMPLE_NAME, districtId);
    }

    @Override
    public DistrictDto getById(Long districtId) {
        District found = Checker.getNonNullObject(repository, districtId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);

        return mapper.toDistrictDto(found);
    }

    @Override
    public Slice<DistrictDto> getAll(String nameStartWith, int page, int size) {
        log.debug("Finding {}(e)s with nameStartWith: {}, page: {}, size: {}", ENTITY_SIMPLE_NAME, nameStartWith, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Slice<District> found;
        if (nameStartWith != null && !nameStartWith.isBlank()) {
            found = repository.findByNameStartingWith(nameStartWith, pageable);
        } else {
            found = repository.findAll(pageable);
        }
        log.debug("Found Slice of {}: {}, numberOfElements: {}", ENTITY_SIMPLE_NAME, found, found.getNumberOfElements());

        return found.map(mapper::toDistrictDto);
    }
}
