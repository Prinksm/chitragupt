package com.example.demo.repository.codeableConcept;

import com.example.demo.entity.codeableConcept.CodeSystem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeSystemRepo extends JpaRepository<CodeSystem,Long> {
    Optional<CodeSystem> findBySystemName(String systemUrl);
}
