package ru.pavbatol.myplace.role.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.pavbatol.myplace.role.dto.RoleDto;
import ru.pavbatol.myplace.role.model.Role;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RoleMapper {

    RoleDto toDto(Role found);

    List<RoleDto> toDtos(List<Role> found);
}
