package com.example.demo.entity.userEntity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "common_telecom", schema = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonTelecom {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "common_telecom_id_seq")
    @SequenceGenerator(name = "common_telecom_id_seq", sequenceName = "common_telecom_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 20)
    private String system;

    @Column(nullable = false, length = 100)
    private String value;

    @Column(name = "use_code", length = 20)
    private String useCode;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
