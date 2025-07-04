package ru.pavbatol.myplace.geo.district.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pavbatol.myplace.geo.common.pagination.Sliced;
import ru.pavbatol.myplace.app.util.Checker;
import ru.pavbatol.myplace.geo.district.mapper.DistrictMapper;
import ru.pavbatol.myplace.geo.district.model.District;
import ru.pavbatol.myplace.geo.district.repository.DistrictRepository;
import ru.pavbatol.myplace.geo.region.mapper.RegionMapper;
import ru.pavbatol.myplace.geo.region.model.Region;
import ru.pavbatol.myplace.geo.region.repository.RegionRepository;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.profile.geo.district.DistrictDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {
    private static final String ENTITY_SIMPLE_NAME = District.class.getSimpleName();
    private final DistrictRepository repository;
    private final RegionRepository regionRepository;
    private final DistrictMapper mapper;
    private final RegionMapper regionMapper;

    @Transactional
    @Override
    public DistrictDto create(DistrictDto dto) {
        District entity = mapper.toEntity(dto, regionRepository, regionMapper);
        District saved = repository.save(entity);
        log.debug("Saved {}: {}", ENTITY_SIMPLE_NAME, saved);

        return mapper.toDistrictDto(saved);
    }

    @Transactional
    @Override
    public DistrictDto update(Long districtId, DistrictDto dto) {
        District original = Checker.getNonNullObject(repository, districtId);

        if (dto.getRegion() != null) {
            if (dto.getRegion().getId() == null) {
                throw new IllegalArgumentException("Region.id in DistrictDto cannot be null when Region provided on updating a District");
            }
            if (!original.getRegion().getId().equals(dto.getRegion().getId())) {
                Region region = Checker.getNonNullObject(regionRepository, dto.getRegion().getId());
                original.setRegion(region);
            }
        }

        District updated = mapper.updateEntity(original, dto);
        updated = repository.save(updated);
        log.debug("Updated {}: {}", ENTITY_SIMPLE_NAME, updated);

        return mapper.toDistrictDto(updated);
    }

    @Transactional
    @Override
    public void delete(Long districtId) {
        repository.deleteById(districtId);
        log.debug("Deleted {}: with districtId: #{}", ENTITY_SIMPLE_NAME, districtId);
    }

    @Transactional(readOnly = true)
    @Override
    public DistrictDto getById(Long districtId) {
        District found = Checker.getNonNullObject(repository, districtId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);

        return mapper.toDistrictDto(found);
    }

    @Transactional(readOnly = true)
    @Override
    public SimpleSlice<DistrictDto> getAll(String nameStartWith, String lastSeenName, Long lastSeenId, int size) {
        log.debug("Finding {}(e)s with nameStartWith: {}, lastSeenName: {}, lastSeenId: {}, size: {}",
                ENTITY_SIMPLE_NAME, nameStartWith, lastSeenName, lastSeenId, size);

        Slice<District> slice = repository.findPageByNamePrefixIgnoreCase(nameStartWith, lastSeenName, lastSeenId, size);
        log.debug("Found {} {}(e)s, hasNext: {}", slice.getNumberOfElements(), ENTITY_SIMPLE_NAME, slice.hasNext());

        return Sliced.from(slice, mapper::toDistrictDto);
    }
}
