package com.example.demo.repository.codeableConcept;

import com.example.demo.entity.codeableConcept.CodeSystem;
import com.example.demo.entity.codeableConcept.ConceptCodeMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConceptCodeMapperRepo extends JpaRepository<ConceptCodeMapper,String> {
}
