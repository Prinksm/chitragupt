package com.example.demo.profile.repository;

import com.example.demo.entity.patientEntity.PatientAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PatientAddressRepository extends JpaRepository<PatientAddress,Long> {
    @Query("SELECT pa FROM PatientAddress pa WHERE pa.id = :addressId AND pa.patient.id = :patientId")
    Optional<PatientAddress> findByIdAndPatientId(@Param("addressId") Long addressId, @Param("patientId") Long patientId);

    @Query("SELECT COUNT(pa) FROM PatientAddress pa WHERE pa.patient.id = :patientId")
    long countByPatientId(@Param("patientId") Long patientId);

    boolean existsByAddressId(Long addressId);
}
