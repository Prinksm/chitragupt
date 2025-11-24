package com.example.demo.entity.userEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles", schema = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
