package ru.pavbatol.myplace.role.service;

import ru.pavbatol.myplace.role.dto.RoleDto;

import java.util.List;

public interface RoleService {

    RoleDto findById(Long roleId);

    List<RoleDto> findAll();
}
