package com.example.demo.entity.medications;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prescription_share_token", schema = "medication")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShareToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String token;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "prescription_ids", columnDefinition = "jsonb", nullable = false)
    private String prescriptionIdsJson;

    @Column(name = "contact_id")
    private Long contactId;

    @Column(name = "contact_value")
    private String contactValue;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}

