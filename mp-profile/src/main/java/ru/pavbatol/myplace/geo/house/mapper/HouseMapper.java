package ru.pavbatol.myplace.geo.house.mapper;

import org.mapstruct.*;
import ru.pavbatol.myplace.app.util.Checker;
import ru.pavbatol.myplace.geo.house.model.House;
import ru.pavbatol.myplace.geo.street.mapper.StreetMapper;
import ru.pavbatol.myplace.geo.street.model.Street;
import ru.pavbatol.myplace.geo.street.repository.StreetRepository;
import ru.pavbatol.myplace.shared.dto.profile.geo.house.HouseDto;
import ru.pavbatol.myplace.shared.dto.profile.geo.street.StreetDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = StreetMapper.class)
public interface HouseMapper {
    HouseDto toHouseDto(House entity);

    House toEntity(HouseDto dto);

    @Mapping(target = "street", source = "dto.street", qualifiedByName = "getStreet")
    House toEntity(HouseDto dto, @Context StreetRepository repository, @Context StreetMapper mapper);

    @Named("getStreet")
    default Street getStreet(StreetDto dto, @Context StreetRepository repository, @Context StreetMapper mapper) {
        if (dto == null) {
            return null;
        }

        Long streetId = dto.getId();

        return streetId == null
                ? mapper.toEntity(dto)
                : Checker.getNonNullObject(repository, streetId);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "street", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    House updateEntity(@MappingTarget House entity, HouseDto dto);
}
