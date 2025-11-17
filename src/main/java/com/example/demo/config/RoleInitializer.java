package com.example.demo.config;

import com.example.demo.entity.userEntity.Roles;
import com.example.demo.repository.RolesRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

    @Autowired
    private RolesRepository rolesRepository;

    @PostConstruct
    public void initRoles() {
        String[] roleNames = {"USER", "ADMIN", "PATIENT"};

        for (String roleName : roleNames) {
            if (!rolesRepository.existsByName(roleName)) {
                Roles role = new Roles();
                role.setName(roleName);
                rolesRepository.save(role);
            }
        }
    }
}
