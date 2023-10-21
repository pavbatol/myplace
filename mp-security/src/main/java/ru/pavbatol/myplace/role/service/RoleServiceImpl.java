package ru.pavbatol.myplace.role.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.app.exception.NotFoundException;
import ru.pavbatol.myplace.role.dto.RoleDto;
import ru.pavbatol.myplace.role.mapper.RoleMapper;
import ru.pavbatol.myplace.role.model.Role;
import ru.pavbatol.myplace.role.repository.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private static final String ENTITY_SIMPLE_NAME = RoleService.class.getSimpleName();
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;


    @Override
    public RoleDto findById(Long roleId) {
        Role found = getNonNullObject(roleRepository, roleId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);
        return roleMapper.toDto(found);
    }

    @Override
    public List<RoleDto> findAll() {
        Iterable<Role> iterable = roleRepository.findAll();
        List<Role> found = StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
        log.debug("Found {}'s number: {}", ENTITY_SIMPLE_NAME, found.size());
        return roleMapper.toDtos(found);
    }

    private Role getNonNullObject(RoleRepository roleRepository, Long roleId) {
        return roleRepository.findById(roleId).orElseThrow(
                () -> new NotFoundException(String.format("%s with id #%s not found", ENTITY_SIMPLE_NAME, roleId))
        );
    }
}
