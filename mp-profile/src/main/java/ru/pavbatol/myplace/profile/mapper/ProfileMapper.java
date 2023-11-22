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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "avatar", expression = "java(decodeBase64(dto.getEncodedAvatar()))")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Profile updateEntity(@MappingTarget Profile entity, ProfileDtoUpdate dto);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "userUuid", source = "userUuid")
    @Mapping(target = "encodedAvatar", expression = "java(encodeBase64(entity.getAvatar()))")
    ProfileDto toProfileDto(Profile entity, UUID userUuid);

    @Mapping(target = "encodedAvatar", expression = "java(encodeBase64(entity.getAvatar()))")
    ProfileDto toProfileDto(Profile entity);

    default byte[] decodeBase64(String encodedString) {
        return encodedString == null ? null : Base64.getDecoder().decode(encodedString);
    }

    default String encodeBase64(byte[] bytes) {
        return bytes == null ? null : Base64.getEncoder().encodeToString(bytes);
    }
}
