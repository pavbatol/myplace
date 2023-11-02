package ru.pavbatol.myplace.profile.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.pavbatol.myplace.profile.dto.ProfileDtoCreateRequest;
import ru.pavbatol.myplace.profile.dto.ProfileDtoCreateResponse;
import ru.pavbatol.myplace.profile.dto.ProfileDtoUpdateStatus;
import ru.pavbatol.myplace.profile.model.Profile;

import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ProfileMapper {

    Profile toEntity(ProfileDtoCreateRequest dto);

    @Mapping(target = "userUuid", source = "userUuid")
    ProfileDtoCreateResponse toDtoCreateResponse(Profile entity, UUID userUuid);

    @Mapping(target = "userUuid", source = "userUuid")
    ProfileDtoUpdateStatus toDtoUpdateStatus(Profile entity, UUID userUuid);
}
