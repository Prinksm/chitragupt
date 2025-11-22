package com.example.demo.repository.AuthCommon;

import com.example.demo.entity.userEntity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByName(String name);
    boolean existsByName(String name); // Add this method
}
