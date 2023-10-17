package ru.pavbatol.myplace.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.pavbatol.myplace.user.dto.UserDtoRegistry;
import ru.pavbatol.myplace.user.model.UserUnverified;

@Mapper(componentModel = "spring", unmappedTargetPolicy =  ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(target = "code", source = "code")
    @Mapping(target = "password", source = "password")
    UserUnverified toDtoUnverified(UserDtoRegistry dtoRegistry, String code, String password);
}
