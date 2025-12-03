package com.example.demo.patientContact.repository;

import com.example.demo.entity.patientEntity.ContactTelecom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
