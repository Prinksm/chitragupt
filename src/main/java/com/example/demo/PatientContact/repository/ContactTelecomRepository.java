package com.example.demo.PatientContact.repository;

import com.example.demo.entity.patientEntity.ContactTelecom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactTelecomRepository extends JpaRepository<ContactTelecom, Long> {
    List<ContactTelecom> findByContactId(Long contactId);
}
