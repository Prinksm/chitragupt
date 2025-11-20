package com.example.demo.PatientContact.repository;

import com.example.demo.entity.patientEntity.ContactAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactAddressRepository extends JpaRepository<ContactAddress, Long> {
    List<ContactAddress> findByContactId(Long contactId);
}

