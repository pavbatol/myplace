package ru.pavbatol.myplace.geo.city.mapper;

import org.mapstruct.*;
import ru.pavbatol.myplace.app.util.Checker;
import ru.pavbatol.myplace.geo.city.model.City;
import ru.pavbatol.myplace.geo.district.mapper.DistrictMapper;
import ru.pavbatol.myplace.geo.district.model.District;
import ru.pavbatol.myplace.geo.district.repository.DistrictRepository;
import ru.pavbatol.myplace.shared.dto.profile.geo.city.CityDto;
import ru.pavbatol.myplace.shared.dto.profile.geo.district.DistrictDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = DistrictMapper.class)
public interface CityMapper {
    CityDto toCityDto(City entity);

    City toEntity(CityDto dto);

    @Mapping(target = "district", source = "dto.district", qualifiedByName = "getDistrict")
    City toEntity(CityDto dto, @Context DistrictRepository repository, @Context DistrictMapper mapper);

    @Named("getDistrict")
    default District getDistrict(DistrictDto dto, @Context DistrictRepository repository, @Context DistrictMapper mapper) {
        if (dto == null) {
            return null;
        }

        Long id = dto.getId();

        return id == null
                ? mapper.toEntity(dto)
                : Checker.getNonNullObject(repository, id);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "district", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    City updateEntity(@MappingTarget City entity, CityDto dto);
}
