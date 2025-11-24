package com.example.demo.repository.codeableConcept;

import com.example.demo.entity.codeableConcept.CodeSystem;
import com.example.demo.entity.codeableConcept.Concepts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConceptRepo extends JpaRepository<Concepts,Long> {
    Optional<Concepts>  findByConceptNameAndType(String display, String type);
}
