package com.example.demo.controller;

import com.example.demo.dto.UpdateRoleDto;
import com.example.demo.entity.userEntity.Roles;
import com.example.demo.entity.userEntity.User;
import com.example.demo.repository.AuthCommon.RolesRepository;
import com.example.demo.repository.AuthCommon.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolesRepository rolesRepository;

    @PostMapping("/update-role")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<String>updateUserRole(@RequestBody UpdateRoleDto req){
        User user = userRepository.findById(req.getUserId()).orElseThrow(()-> new RuntimeException("user not found"));
        Roles role = rolesRepository.findByName(req.getRoleName()).orElseThrow(()-> new RuntimeException("role not found"));
        user.getRoles().add(role);
        userRepository.save(user);
        return ResponseEntity.ok("Role updated successfully");
    }
}
