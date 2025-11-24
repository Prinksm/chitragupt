package com.example.demo.addMedication.repo;

import com.example.demo.entity.medications.Timing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimingRepo extends JpaRepository<Timing,Long> {
}
