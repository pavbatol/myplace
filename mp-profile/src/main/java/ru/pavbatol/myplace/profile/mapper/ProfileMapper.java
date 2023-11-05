package ru.pavbatol.myplace.profile.mapper;

import org.mapstruct.*;
import ru.pavbatol.myplace.profile.dto.*;
import ru.pavbatol.myplace.profile.model.Profile;

import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ProfileMapper {

    Profile toEntity(ProfileDtoCreateRequest dto);

    @Mapping(target = "userUuid", source = "userUuid")
    ProfileDtoCreateResponse toDtoCreateResponse(Profile entity, UUID userUuid);

    @Mapping(target = "userUuid", source = "userUuid")
    ProfileDtoUpdateStatusResponse toDtoUpdateStatusResponse(Profile entity, UUID userUuid);

    @Mapping(target = "userId", source = "userId")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Profile updateEntity(@MappingTarget Profile entity, ProfileDtoUpdate dto, Long userId);

    @Mapping(target = "userUuid", source = "userUuid")
    ProfileDto toProfileDto(Profile entity, UUID userUuid);
}
