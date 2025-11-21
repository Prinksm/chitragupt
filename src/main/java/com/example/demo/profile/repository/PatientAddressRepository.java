package com.example.demo.profile.repository;

import com.example.demo.entity.patientEntity.PatientAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientAddressRepository extends JpaRepository<PatientAddress,Long> {
}
