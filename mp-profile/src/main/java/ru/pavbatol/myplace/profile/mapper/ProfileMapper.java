package ru.pavbatol.myplace.profile.mapper;

import org.mapstruct.*;
import ru.pavbatol.myplace.profile.dto.*;
import ru.pavbatol.myplace.profile.model.Profile;

import java.util.Base64;
import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ProfileMapper {

    Profile toEntity(ProfileDtoCreateRequest dto);

    @Mapping(target = "userUuid", source = "userUuid")
    ProfileDtoCreateResponse toDtoCreateResponse(Profile entity, UUID userUuid);

    @Mapping(target = "userUuid", source = "userUuid")
    ProfileDtoUpdateStatusResponse toDtoUpdateStatusResponse(Profile entity, UUID userUuid);

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "avatar", expression = "java(decodeBase64(dto.getEncodedAvatar()))")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Profile updateEntity(@MappingTarget Profile entity, ProfileDtoUpdate dto, Long userId);

    @Mapping(target = "userUuid", source = "userUuid")
    @Mapping(target = "encodedAvatar", expression = "java(encodeBase64(entity.getAvatar()))")
    ProfileDto toProfileDto(Profile entity, UUID userUuid);

    default byte[] decodeBase64(String encodedString) {
        return Base64.getDecoder().decode(encodedString);
    }

    default String encodeBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
