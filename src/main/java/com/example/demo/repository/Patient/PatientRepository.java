package com.example.demo.repository.Patient;

import com.example.demo.entity.patientEntity.Patient;
import com.example.demo.entity.userEntity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByFhirIsNull();
    Optional<Patient> findByUser(User user);

    @Query(nativeQuery = true, value = "SELECT * FROM patient.patient WHERE user_id = :userId")
    Optional<Patient> findByUser_Id(@Param("userId") Long userId);


}
