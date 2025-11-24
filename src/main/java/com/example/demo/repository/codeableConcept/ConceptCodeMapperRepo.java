package com.example.demo.repository.codeableConcept;

import com.example.demo.entity.codeableConcept.CodeSystem;
import com.example.demo.entity.codeableConcept.ConceptCodeMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ConceptCodeMapperRepo extends JpaRepository<ConceptCodeMapper,String> {
    Optional<ConceptCodeMapper> findByConceptId(Long conceptId);

    Optional<ConceptCodeMapper> findByCode(String code);
}
