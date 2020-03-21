package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.ERole;
import com.pa.asvblrapi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
