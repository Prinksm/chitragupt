package com.example.demo.entity.userEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "common_address", schema = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "common_address_id_seq")
    @SequenceGenerator(name = "common_address_id_seq", sequenceName = "common_address_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "use_code", length = 20)
    private String useCode;

    @Column(name = "address_type", length = 50)
    private String addressType;

    @Column(length = 255)
    private String text;

    @Column(length = 100)
    private String line1;

    @Column(length = 100)
    private String line2;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String district;

    @Column(length = 50)
    private String state;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(length = 50)
    private String country;

    @Column(name = "period_start")
    private LocalDate periodStart;

    @Column(name = "period_end")
    private LocalDate periodEnd;

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
