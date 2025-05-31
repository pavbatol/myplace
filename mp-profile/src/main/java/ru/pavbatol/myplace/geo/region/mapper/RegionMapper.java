package ru.pavbatol.myplace.geo.region.mapper;

import org.mapstruct.*;
import ru.pavbatol.myplace.app.util.Checker;
import ru.pavbatol.myplace.geo.country.dto.CountryDto;
import ru.pavbatol.myplace.geo.country.mapper.CountryMapper;
import ru.pavbatol.myplace.geo.country.model.Country;
import ru.pavbatol.myplace.geo.country.repository.CountryRepository;
import ru.pavbatol.myplace.geo.region.dto.RegionDto;
import ru.pavbatol.myplace.geo.region.model.Region;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = CountryMapper.class)
public interface RegionMapper {
    RegionDto toRegionDto(Region entity);

    Region toEntity(RegionDto dto);

    @Mapping(target = "country", source = "dto.country", qualifiedByName = "getCountry")
    Region toEntity(RegionDto dto, @Context CountryRepository repository, @Context CountryMapper mapper);

    @Named("getCountry")
    default Country getCountry(CountryDto dto, @Context CountryRepository repository, @Context CountryMapper mapper) {
        if (dto == null) {
            return null;
        }

        Long id = dto.getId();

        return id == null
                ? mapper.toEntity(dto)
                : Checker.getNonNullObject(repository, id);
    }

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Region updateEntity(@MappingTarget Region entity, RegionDto dto);
}
