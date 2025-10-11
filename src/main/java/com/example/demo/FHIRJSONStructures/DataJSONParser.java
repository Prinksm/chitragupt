package com.example.demo.FHIRJSONStructures;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.*;

import java.sql.SQLOutput;
import java.util.List;

public class DataJSONParser {
    public String PatientData(String FhirJson) {

        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        Patient patient = parser.parseResource(Patient.class, FhirJson);
        //Checking for Identifier (	0..*)
        if (patient.hasIdentifier()) {
            List<Identifier> identifiers = patient.getIdentifier();

            for (Identifier identifier : identifiers) {
                if(identifier.hasUse()){
                    String use = String.valueOf(identifier.getUse());
                }
                if(identifier.hasType()){
                    CodeableConcept type = identifier.getType();
                    String TypeText = type.getText();

//Multiple coding system could be present for the system
                    for (Coding coding : type.getCoding()) {
                        System.out.println("  Coding System: " + coding.getSystem());
                        System.out.println("  Coding Code: " + coding.getCode());
                        System.out.println("  Coding Display: " + coding.getDisplay());
                    }
                }
                if(identifier.hasSystem()){
                    System.out.println(" System Url: " + identifier.getSystem());
                }
                if(identifier.hasValue()){
                    System.out.println("Identifier Value" + identifier.hasValue());
                }
                if(identifier.hasPeriod()){
                    Period period = identifier.getPeriod();
                    if(period.hasStart()){
                        System.out.println("Period Start" + period.getStart());
                    }
                    if(period.hasEnd()){
                        System.out.println("Period End"+ period.getEnd());
                    }
                }
                if (identifier.hasAssigner()) {

                    String assignerName = identifier.getAssigner().getDisplay();
                    System.out.println("Assigner Name: " + assignerName);
                }
            }


        }
        if(patient.hasActive()){
            System.out.println("Patient Active"+ patient.getActive());
        }
        //Patient name
        if(patient.hasName()){
            List<HumanName> names  = patient.getName();
            for(HumanName name : names){
                if(name.hasUse()){
                    System.out.println("Name"+ name.getUse());
                }
                if(name.hasText()){
                    System.out.println("Text"+ name.getText());
                }
                if(name.hasFamily()){
                    System.out.println("Family"+name.getFamily());
                }
                if(name.hasGiven()){
                    List<StringType> givenNames = name.getGiven();
                    for(StringType givenName:givenNames){
                        System.out.println("Given Names"+givenName);
                    }
                }
                if(name.hasPrefix()){
                    List<StringType> prefixes = name.getPrefix();
                    for(StringType prefix:prefixes){
                        System.out.println("Given Names"+prefix);
                    }
                }
                if(name.hasSuffix()){
                    List<StringType> suffixes = name.getSuffix();
                    for(StringType suffix:suffixes){
                        System.out.println("Given Names"+suffix);
                    }
                }
                if(name.hasPeriod()){
                    Period period = name.getPeriod();
                    if(period.hasStart()){
                        System.out.println("Period Start" + period.getStart());
                    }
                    if(period.hasEnd()){
                        System.out.println("Period End"+ period.getEnd());
                    }
                }
            }
        }
        //Patient Telecom
        if(patient.hasTelecom()){
            List<ContactPoint> contactPoints = patient.getTelecom();
            for(ContactPoint contactPoint:contactPoints){
                if(contactPoint.hasSystem()){
                    String system = String.valueOf(contactPoint.getSystem());
                    System.out.println("System"+system);
                }

            }
        }
        return FhirJson;

    }
}
