package ru.pavbatol.myplace.security.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pavbatol.myplace.security.role.model.Role;
import ru.pavbatol.myplace.security.role.model.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleName roleName);
}
