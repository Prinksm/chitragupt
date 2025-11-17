package com.example.demo.repository;

import com.example.demo.entity.userEntity.CommonAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommonAddressRepository extends JpaRepository<CommonAddress, Long> {
    Optional<CommonAddress> findById(Long id);

    // Custom query to find addresses by patient ID
    @Query("SELECT ca FROM CommonAddress ca JOIN PatientAddress pa ON ca.id = pa.address.Id WHERE pa.patient.Id = :patientId")
    List<CommonAddress> findByPatientId(@Param("patientId") Long patientId);
}
