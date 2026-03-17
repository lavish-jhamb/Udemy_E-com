package com.ecommerce.project.repository;

import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByRoles(Roles roles);

}
