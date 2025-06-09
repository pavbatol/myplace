package ru.pavbatol.myplace.geo.district.mapper;

import org.mapstruct.*;
import ru.pavbatol.myplace.app.util.Checker;
import ru.pavbatol.myplace.geo.district.dto.DistrictDto;
import ru.pavbatol.myplace.geo.district.model.District;
import ru.pavbatol.myplace.geo.region.dto.RegionDto;
import ru.pavbatol.myplace.geo.region.mapper.RegionMapper;
import ru.pavbatol.myplace.geo.region.model.Region;
import ru.pavbatol.myplace.geo.region.repository.RegionRepository;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = RegionMapper.class)
public interface DistrictMapper {
    DistrictDto toDistrictDto(District entity);

    District toEntity(DistrictDto dto);

    @Mapping(target = "region", source = "dto.region", qualifiedByName = "getRegion")
    District toEntity(DistrictDto dto, @Context RegionRepository repository, @Context RegionMapper mapper);

    @Named("getRegion")
    default Region getRegion(RegionDto dto, @Context RegionRepository repository, @Context RegionMapper mapper) {
        if (dto == null) {
            return null;
        }

        Long id = dto.getId();

        return id == null
                ? mapper.toEntity(dto)
                : Checker.getNonNullObject(repository, id);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "region", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    District updateEntity(@MappingTarget District entity, DistrictDto dto);
}
