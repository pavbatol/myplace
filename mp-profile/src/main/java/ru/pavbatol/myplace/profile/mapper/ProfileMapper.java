package ru.pavbatol.myplace.profile.mapper;

import org.mapstruct.*;
import ru.pavbatol.myplace.app.Util.Checker;
import ru.pavbatol.myplace.geo.house.dto.HouseDto;
import ru.pavbatol.myplace.geo.house.mapper.HouseMapper;
import ru.pavbatol.myplace.geo.house.model.House;
import ru.pavbatol.myplace.geo.house.repository.HouseRepository;
import ru.pavbatol.myplace.profile.dto.*;
import ru.pavbatol.myplace.profile.model.Profile;

import java.util.Base64;
import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = HouseMapper.class)
public interface ProfileMapper {

    Profile toEntity(ProfileDtoCreateRequest dto);

    @Mapping(target = "userUuid", source = "userUuid")
    ProfileDtoCreateResponse toDtoCreateResponse(Profile entity, UUID userUuid);

    @Mapping(target = "userUuid", source = "userUuid")
    ProfileDtoUpdateStatusResponse toDtoUpdateStatusResponse(Profile entity, UUID userUuid);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "house", source = "house", qualifiedByName = "getHouse")
    @Mapping(target = "avatar", expression = "java(decodeBase64(dto.getEncodedAvatar()))")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Profile updateEntity(@MappingTarget Profile entity, ProfileDtoUpdate dto,
                         @Context HouseRepository houseRepository,
                         @Context HouseMapper houseMapper);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "userUuid", source = "userUuid")
    @Mapping(target = "encodedAvatar", expression = "java(encodeBase64(entity.getAvatar()))")
    ProfileDto toProfileDto(Profile entity, UUID userUuid);

    @Mapping(target = "house", ignore = true)
    @Mapping(target = "encodedAvatar", expression = "java(encodeBase64(entity.getAvatar()))")
    ProfileDto toProfileDtoWithoutHose(Profile entity);

    default byte[] decodeBase64(String encodedString) {
        return encodedString == null ? null : Base64.getDecoder().decode(encodedString);
    }

    default String encodeBase64(byte[] bytes) {
        return bytes == null ? null : Base64.getEncoder().encodeToString(bytes);
    }

    @Named("getHouse")
    default House getStreet(HouseDto dto, @Context HouseRepository repository, @Context HouseMapper mapper) {
        if (dto == null) {
            return null;
        }

        Long houseId = dto.getId();

        return houseId == null
                ? mapper.toEntity(dto)
                : Checker.getNonNullObject(repository, houseId);
    }
}
