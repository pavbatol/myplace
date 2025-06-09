package ru.pavbatol.myplace.geo.street.mapper;

import org.mapstruct.*;
import ru.pavbatol.myplace.app.util.Checker;
import ru.pavbatol.myplace.geo.city.dto.CityDto;
import ru.pavbatol.myplace.geo.city.mapper.CityMapper;
import ru.pavbatol.myplace.geo.city.model.City;
import ru.pavbatol.myplace.geo.city.repository.CityRepository;
import ru.pavbatol.myplace.geo.street.dto.StreetDto;
import ru.pavbatol.myplace.geo.street.model.Street;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = CityMapper.class)
public interface StreetMapper {
    StreetDto toStreetDto(Street entity);

    Street toEntity(StreetDto dto);

    @Mapping(target = "city", source = "dto.city", qualifiedByName = "getCity")
    Street toEntity(StreetDto dto, @Context CityRepository repository, @Context CityMapper mapper);

    @Named("getCity")
    default City getCity(CityDto dto, @Context CityRepository repository, @Context CityMapper mapper) {
        if (dto == null) {
            return null;
        }

        Long id = dto.getId();

        return id == null
                ? mapper.toEntity(dto)
                : Checker.getNonNullObject(repository, id);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Street updateEntity(@MappingTarget Street entity, StreetDto dto);
}
