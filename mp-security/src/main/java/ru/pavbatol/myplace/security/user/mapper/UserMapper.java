package ru.pavbatol.myplace.security.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.pavbatol.myplace.shared.dto.security.user.UserDtoRegistry;
import ru.pavbatol.myplace.shared.dto.security.user.UserDtoResponse;
import ru.pavbatol.myplace.security.user.model.User;
import ru.pavbatol.myplace.security.user.model.UserUnverified;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(target = "code", source = "code")
    @Mapping(target = "password", source = "password")
    UserUnverified toUserUnverified(UserDtoRegistry dtoRegistry, String code, String password);

    UserDtoResponse toResponseDto(User user);

    List<UserDtoResponse> toResponseDtos(List<User> users);
}
