package ru.pavbatol.myplace.security.role.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.pavbatol.myplace.security.role.model.Role;
import ru.pavbatol.myplace.shared.dto.security.role.RoleDto;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RoleMapper {

    RoleDto toDto(Role found);

    List<RoleDto> toDtos(List<Role> found);
}
