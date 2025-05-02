package ru.pavbatol.myplace.security.role.service;


import ru.pavbatol.myplace.shared.dto.security.role.RoleDto;

import java.util.List;

public interface RoleService {

    RoleDto findById(Long roleId);

    List<RoleDto> findAll();
}
