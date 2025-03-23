package com.ortim.repository;

import com.ortim.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findRoleByNameEqualsIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    Role findByName(String name);

}
