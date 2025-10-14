package com.example.demo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.example.demo.FHIRJSONStructures.DataJSONParser;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("api/patient")
public class PatientController {
    @Autowired
    DataJSONParser dataJSONParser;
    @GetMapping("/describe")
    public String describePatient(){
        Patient patient = new Patient();
//        CodeableConcept maritalStatus = new CodeableConcept();

//        patient.setMaritalStatus()
        patient.addName(new HumanName().setFamily("Shree")
                .addGiven("Adishree")
                .addGiven("Adi"));
        patient.setGender(Enumerations.AdministrativeGender.FEMALE);
        patient.setBirthDate(new Date(2004,3,28));
        patient.addTelecom(new ContactPoint().setSystem(ContactPoint.ContactPointSystem.PHONE));
        patient.addAddress(new Address()
                .setCity("Cuttack")
                .setState("Odisha")
                );
        FhirContext ctx = FhirContext.forR4();
        String FhirJSON = ctx.newJsonParser()
                .setPrettyPrint(true)
                .encodeResourceToString(patient);
        System.out.println(FhirJSON);

        IParser parser = ctx.newJsonParser();
        Patient patient1= parser.parseResource(Patient.class,FhirJSON);
        String patientId = patient1.getName().getFirst().getFamily();
        String gender = patient1.getGender().toCode();

        System.out.println("Patient ID: " + patientId);
        System.out.println("Gender: " + gender);
        return FhirJSON;
    }


    @GetMapping("/FhirParser/Patient")
    public String FhirParser(@RequestBody String FhirJson){
        return dataJSONParser.PatientData(FhirJson);

    }
}
