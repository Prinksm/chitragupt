package com.example.demo.patientContact.repository;

import com.example.demo.entity.patientEntity.ContactTelecom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactTelecomRepository extends JpaRepository<ContactTelecom, Long> {
    List<ContactTelecom> findByContactId(Long contactId);
    @Query("""
         SELECT ct
                 FROM ContactTelecom ct
                 JOIN ct.telecom t
                 WHERE LOWER(t.value) = LOWER(:email)
    """)
    List<ContactTelecom> findByEmail(String email);
    long countByTelecomId(Long addressId);


    @Query("""
        SELECT COUNT(ct) > 0
        FROM ContactTelecom ct
        JOIN ct.telecom t
        JOIN ct.contact c
        JOIN c.patient p
        WHERE p.id = :patientId
          AND t.value = :email
          AND t.system = 'email'
    """)
    boolean existsEmergencyContactForPatient(
            @Param("email") String email,
            @Param("patientId") Long patientId
    );
}
