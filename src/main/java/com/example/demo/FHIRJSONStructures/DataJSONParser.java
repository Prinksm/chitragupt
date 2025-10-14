package com.example.demo.FHIRJSONStructures;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class DataJSONParser {
    public String PatientData(String FhirJson) {

        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        Patient patient = parser.parseResource(Patient.class, FhirJson);
        //Checking for Identifier (	0..*)
        if (patient.hasIdentifier()) {
            List<Identifier> identifiers = patient.getIdentifier();

            for (Identifier identifier : identifiers) {
                if (identifier.hasUse()) {
                    String use = String.valueOf(identifier.getUse());
                }
                if (identifier.hasType()) {
                    CodeableConcept type = identifier.getType();
                    String TypeText = type.getText();

//Multiple coding system could be present for the system
                    for (Coding coding : type.getCoding()) {
                        System.out.println("  Coding System: " + coding.getSystem());
                        System.out.println("  Coding Code: " + coding.getCode());
                        System.out.println("  Coding Display: " + coding.getDisplay());
                    }
                }
                if (identifier.hasSystem()) {
                    System.out.println(" System Url: " + identifier.getSystem());
                }
                if (identifier.hasValue()) {
                    System.out.println("Identifier Value :" + identifier.hasValue());
                }
                if (identifier.hasPeriod()) {
                    Period period = identifier.getPeriod();
                    if (period.hasStart()) {
                        System.out.println("Period Start:" + period.getStart());
                    }
                    if (period.hasEnd()) {
                        System.out.println("Period End:" + period.getEnd());
                    }
                }
                if (identifier.hasAssigner()) {

                    String assignerName = identifier.getAssigner().getDisplay();
                    System.out.println("Assigner Name: " + assignerName);
                }
            }


        }
        if (patient.hasActive()) {
            System.out.println("Patient Active :" + patient.getActive());
        }
        //Patient name
        if (patient.hasName()) {
            List<HumanName> names = patient.getName();
            for (HumanName name : names) {
                if (name.hasUse()) {
                    System.out.println("Name :" + name.getUse());
                }
                if (name.hasText()) {
                    System.out.println("Text :" + name.getText());
                }
                if (name.hasFamily()) {
                    System.out.println("Family :" + name.getFamily());
                }
                if (name.hasGiven()) {
                    List<StringType> givenNames = name.getGiven();
                    for (StringType givenName : givenNames) {
                        System.out.println("Given Names :" + givenName);
                    }
                }
                if (name.hasPrefix()) {
                    List<StringType> prefixes = name.getPrefix();
                    for (StringType prefix : prefixes) {
                        System.out.println("Given Names :" + prefix);
                    }
                }
                if (name.hasSuffix()) {
                    List<StringType> suffixes = name.getSuffix();
                    for (StringType suffix : suffixes) {
                        System.out.println("Given Names :" + suffix);
                    }
                }
                if (name.hasPeriod()) {
                    Period period = name.getPeriod();
                    if (period.hasStart()) {
                        System.out.println("Period Start :" + period.getStart());
                    }
                    if (period.hasEnd()) {
                        System.out.println("Period End :" + period.getEnd());
                    }
                }
            }
        }
        //Patient Telecom
        if (patient.hasTelecom()) {
            List<ContactPoint> contactPoints = patient.getTelecom();
            for (ContactPoint contactPoint : contactPoints) {
                if (contactPoint.hasSystem()) {
                    String system = String.valueOf(contactPoint.getSystem());
                    System.out.println("System" + system);
                }
                if (contactPoint.hasValue()) {
                    String value = contactPoint.getValue();
                    System.out.println("Value:" + value);
                }
                if (contactPoint.hasUse()) {
                    String use = String.valueOf(contactPoint.getUse());
                    System.out.println("Use" + use);
                }
                if (contactPoint.hasRank()) {
                    int rank = contactPoint.getRank();
                    System.out.println("Rank" + rank);
                }
                if (contactPoint.hasPeriod()) {
                    Period period = contactPoint.getPeriod();
                    if (period.hasStart()) {
                        System.out.println("Period Start" + period.getStart());
                    }
                    if (period.hasEnd()) {
                        System.out.println("Period End" + period.getEnd());
                    }
                }

            }
        }

        //Patient Gender
        if (patient.hasGender()) {
            String gender = String.valueOf(patient.getGender());
            System.out.println("Gender:" + gender);
        }
        //patient Birthdate
        if (patient.hasBirthDate()) {
            Date date = patient.getBirthDate();
            System.out.println("BirthDate:" + date);
        }
        //patient Deceased
        if (patient.hasDeceased()) {
            BooleanType booleanType = patient.getDeceasedBooleanType();
            if (booleanType.booleanValue()) {
                System.out.println("Deceased time" + patient.getDeceasedDateTimeType());
            }
        }
        //patient Address
        if (patient.hasAddress()) {
            List<Address> addresses = patient.getAddress();
            for (Address address : addresses) {
                if (address.hasUse()) {
                    System.out.println("Address Use:" + address.getUse());
                }
                if (address.hasType()) {
                    System.out.println("Address Type:" + address.getType());
                }
                if (address.hasText()) {
                    System.out.println("Address Text:" + address.getText());
                }
                if (address.hasLine()) {
                    List<StringType> lines = address.getLine();
                    for (StringType line : lines) {
                        System.out.println("Address Line : " + line);
                    }
                }
                if (address.hasCity()) {
                    System.out.println("Address City:" + address.getCity());
                }
                if (address.hasDistrict()) {
                    System.out.println("Address District:" + address.getDistrict());
                }
                if (address.hasState()) {
                    System.out.println("Address State:" + address.getState());
                }
                if (address.hasPostalCode()) {
                    System.out.println("Address Postal Code" + address.getPostalCode());
                }
                if (address.hasCountry()) {
                    System.out.println("Address Country" + address.getCountry());
                }
                if (address.hasPeriod()) {
                    Period period = address.getPeriod();
                    if (period.hasStart()) {
                        System.out.println("Period Start" + period.getStart());
                    }
                    if (period.hasEnd()) {
                        System.out.println("Period End" + period.getEnd());
                    }
                }
            }
        }

        //patient Marital Status
        if (patient.hasMaritalStatus()) {
            CodeableConcept MaritalStatus = patient.getMaritalStatus();

            if (MaritalStatus.hasText()) {
                System.out.println("Marital Status Text :" + MaritalStatus.getText());
            }
            if (MaritalStatus.hasCoding()) {
                List<Coding> codes = MaritalStatus.getCoding();
                for (Coding code : codes) {
                    if (code.hasSystem()) {
                        System.out.println("Code System" + code.getSystem());
                    }
                    if (code.hasCode()) {
                        System.out.println("Code:" + code.getCode());
                    }
                    if (code.hasVersion()) {
                        System.out.println("Code element:" + code.getVersion());
                    }
                    if (code.hasDisplay()) {
                        System.out.println("Code Display:" + code.getDisplay());
                    }

                }

            }

        }

        //patient multiple birth
        if (patient.hasMultipleBirth()) {

            if (patient.getMultipleBirth() instanceof BooleanType) {
                BooleanType multipleBirthBoolean = patient.getMultipleBirthBooleanType();
                boolean isMultipleBirth = multipleBirthBoolean.booleanValue();
                System.out.println("Is part of a multiple birth: " + isMultipleBirth);
            }
            // Check if the multipleBirth field is of type Integer
            else if (patient.getMultipleBirth() instanceof IntegerType) {
                IntegerType multipleBirthInteger = patient.getMultipleBirthIntegerType();
                // Now you can safely get the integer value
                int birthOrder = multipleBirthInteger.getValue();
                System.out.println("Multiple birth order: " + birthOrder);
            }
        }
        //patient photo
        if (patient.hasPhoto()) {
            List<Attachment> photos = patient.getPhoto();
            for (Attachment photo : photos) {
                System.out.println("Attachment :" + photo.getUrl());
                System.out.println("Attachment Base64 :" + Arrays.toString(photo.getData()));
            }
        }
        //patient contact
        if (patient.hasContact()) {
            List<Patient.ContactComponent> Contacts = patient.getContact();
            for (Patient.ContactComponent contact : Contacts) {
                if (contact.hasRelationship()) {
                    List<CodeableConcept> relationships = contact.getRelationship();
                    for (CodeableConcept relation : relationships) {
                        if (relation.hasText()) {
                            System.out.println("Relation Text :" + relation.getText());
                        }
                        if (relation.hasCoding()) {
                            List<Coding> codes = relation.getCoding();
                            for (Coding code : codes) {
                                if (code.hasSystem()) {
                                    System.out.println("Code System" + code.getSystem());
                                }
                                if (code.hasCode()) {
                                    System.out.println("Code:" + code.getCode());
                                }
                                if (code.hasVersion()) {
                                    System.out.println("Code element:" + code.getVersion());
                                }
                                if (code.hasDisplay()) {
                                    System.out.println("Code Display:" + code.getDisplay());
                                }

                            }

                        }

                    }
                }
                if (contact.hasName()) {
                    HumanName name = contact.getName();
                    if (name.hasUse()) {
                        System.out.println("Name" + name.getUse());
                    }
                    if (name.hasText()) {
                        System.out.println("Text" + name.getText());
                    }
                    if (name.hasFamily()) {
                        System.out.println("Family" + name.getFamily());
                    }
                    if (name.hasGiven()) {
                        List<StringType> givenNames = name.getGiven();
                        for (StringType givenName : givenNames) {
                            System.out.println("Given Names" + givenName);
                        }
                    }
                    if (name.hasPrefix()) {
                        List<StringType> prefixes = name.getPrefix();
                        for (StringType prefix : prefixes) {
                            System.out.println("Given Names" + prefix);
                        }
                    }
                    if (name.hasSuffix()) {
                        List<StringType> suffixes = name.getSuffix();
                        for (StringType suffix : suffixes) {
                            System.out.println("Given Names" + suffix);
                        }
                    }
                    if (name.hasPeriod()) {
                        Period period = name.getPeriod();
                        if (period.hasStart()) {
                            System.out.println("Period Start" + period.getStart());
                        }
                        if (period.hasEnd()) {
                            System.out.println("Period End" + period.getEnd());
                        }
                    }
                }
                if (contact.hasTelecom()) {
                    List<ContactPoint> telecom = contact.getTelecom();
                    for (ContactPoint contactPoint : telecom) {
                        if (contactPoint.hasSystem()) {
                            String system = String.valueOf(contactPoint.getSystem());
                            System.out.println("System" + system);
                        }
                        if (contactPoint.hasValue()) {
                            String value = contactPoint.getValue();
                            System.out.println("Value:" + value);
                        }
                        if (contactPoint.hasUse()) {
                            String use = String.valueOf(contactPoint.getUse());
                            System.out.println("Use" + use);
                        }
                        if (contactPoint.hasRank()) {
                            int rank = contactPoint.getRank();
                            System.out.println("Rank" + rank);
                        }
                        if (contactPoint.hasPeriod()) {
                            Period period = contactPoint.getPeriod();
                            if (period.hasStart()) {
                                System.out.println("Period Start" + period.getStart());
                            }
                            if (period.hasEnd()) {
                                System.out.println("Period End" + period.getEnd());
                            }
                        }

                    }
                }
                if (contact.hasAddress()) {
                    Address address = contact.getAddress();

                    if (address.hasUse()) {
                        System.out.println("Address Use:" + address.getUse());
                    }
                    if (address.hasType()) {
                        System.out.println("Address Type:" + address.getType());
                    }
                    if (address.hasText()) {
                        System.out.println("Address Text:" + address.getText());
                    }
                    if (address.hasLine()) {
                        List<StringType> lines = address.getLine();
                        for (StringType line : lines) {
                            System.out.println("Address Line : " + line);
                        }
                    }
                    if (address.hasCity()) {
                        System.out.println("Address City:" + address.getCity());
                    }
                    if (address.hasDistrict()) {
                        System.out.println("Address District:" + address.getDistrict());
                    }
                    if (address.hasState()) {
                        System.out.println("Address State:" + address.getState());
                    }
                    if (address.hasPostalCode()) {
                        System.out.println("Address Postal Code" + address.getPostalCode());
                    }
                    if (address.hasCountry()) {
                        System.out.println("Address Country" + address.getCountry());
                    }
                    if (address.hasPeriod()) {
                        Period period = address.getPeriod();
                        if (period.hasStart()) {
                            System.out.println("Period Start" + period.getStart());
                        }
                        if (period.hasEnd()) {
                            System.out.println("Period End" + period.getEnd());
                        }

                    }
                }
                if (contact.hasGender()) {
                    System.out.println("Contact Gender:" + contact.getGender());
                }
                if (contact.hasOrganization()) {
                    System.out.println("Organizations" + contact.getOrganization());
                }
                if (contact.hasPeriod()) {
                    Period period = contact.getPeriod();
                    if (period.hasStart()) {
                        System.out.println("Period Start" + period.getStart());
                    }
                    if (period.hasEnd()) {
                        System.out.println("Period End" + period.getEnd());
                    }
                }

            }
        }

        //patient Communication
        if (patient.hasCommunication()) {
            List<Patient.PatientCommunicationComponent> communications = patient.getCommunication();
            for (Patient.PatientCommunicationComponent communication : communications) {
                CodeableConcept language = communication.getLanguage();

                if (language.hasText()) {
                    System.out.println("Language Text :" + language.getText());
                }
                if (language.hasCoding()) {
                    List<Coding> codes = language.getCoding();
                    for (Coding code : codes) {
                        if (code.hasSystem()) {
                            System.out.println("Code System :" + code.getSystem());
                        }
                        if (code.hasCode()) {
                            System.out.println("Code:" + code.getCode());
                        }
                        if (code.hasVersion()) {
                            System.out.println("Code element:" + code.getVersion());
                        }
                        if (code.hasDisplay()) {
                            System.out.println("Code Display:" + code.getDisplay());
                        }

                    }

                }
                System.out.println("Preference Language :" + communication.getPreferred());

            }
        }
        //general practitioner
        if (patient.hasGeneralPractitioner()) {
            for (Reference gpRef : patient.getGeneralPractitioner()) {
                String referenceUrl = gpRef.getReference();
                String display = gpRef.getDisplay();
                System.out.println("General Practitioner Reference: " + referenceUrl);
                if (display != null) {
                    System.out.println("Display Name: " + display);
                }
            }

        }
        //patient general Organization
        if (patient.hasManagingOrganization()) {
            Reference orgRef = patient.getManagingOrganization();
            String referenceUrl = orgRef.getReference();
            String display = orgRef.getDisplay();
            System.out.println("Managing Organization Reference: " + referenceUrl);
            if (display != null) {
                System.out.println("Display Name: " + display);
            }
        }


        return FhirJson;
    }

    public String MedicationRequestData(String FhirJson) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        MedicationRequest medicationRequest = parser.parseResource(MedicationRequest.class, FhirJson);
        if (medicationRequest.hasIdentifier()) {
            List<Identifier> identifiers = medicationRequest.getIdentifier();

            for (Identifier identifier : identifiers) {
                if (identifier.hasUse()) {
                    String use = String.valueOf(identifier.getUse());
                }
                if (identifier.hasType()) {
                    CodeableConcept type = identifier.getType();
                    String TypeText = type.getText();

//Multiple coding system could be present for the system
                    for (Coding coding : type.getCoding()) {
                        System.out.println("  Coding System: " + coding.getSystem());
                        System.out.println("  Coding Code: " + coding.getCode());
                        System.out.println("  Coding Display: " + coding.getDisplay());
                    }
                }
                if (identifier.hasSystem()) {
                    System.out.println(" System Url: " + identifier.getSystem());
                }
                if (identifier.hasValue()) {
                    System.out.println("Identifier Value :" + identifier.hasValue());
                }
                if (identifier.hasPeriod()) {
                    Period period = identifier.getPeriod();
                    if (period.hasStart()) {
                        System.out.println("Period Start:" + period.getStart());
                    }
                    if (period.hasEnd()) {
                        System.out.println("Period End:" + period.getEnd());
                    }
                }
                if (identifier.hasAssigner()) {

                    String assignerName = identifier.getAssigner().getDisplay();
                    System.out.println("Assigner Name: " + assignerName);
                }
            }


        }
        //Based On
        if (medicationRequest.hasBasedOn()) {
            List<Reference> basedDatas = medicationRequest.getBasedOn();
            for (Reference basedOn : basedDatas) {
                System.out.println("Reference Based on: " + basedOn.getReference());
                System.out.println("Display Based on:" + basedOn.getDisplay());
            }
        }
        //Prior Prescription
        if (medicationRequest.hasPriorPrescription()) {
            Reference prescription = medicationRequest.getPriorPrescription();
            System.out.println("Prescription:" + prescription.getReference());
            System.out.println("Prescription display:" + prescription.getDisplay());
        }
        //groupIdentifier
        if (medicationRequest.hasGroupIdentifier()) {
            Identifier identifier = medicationRequest.getGroupIdentifier();
            if (identifier.hasUse()) {
                String use = String.valueOf(identifier.getUse());
                System.out.println("Group Identifier Use : "+ use);
            }
            if (identifier.hasType()) {
                CodeableConcept type = identifier.getType();
                String TypeText = type.getText();
                System.out.println("Group Identifier Text :"+TypeText);
//Multiple coding system could be present for the system
                for (Coding coding : type.getCoding()) {
                    System.out.println("  Coding System: " + coding.getSystem());
                    System.out.println("  Coding Code: " + coding.getCode());
                    System.out.println("  Coding Display: " + coding.getDisplay());
                }
            }
            if (identifier.hasSystem()) {
                System.out.println(" System Url: " + identifier.getSystem());
            }
            if (identifier.hasValue()) {
                System.out.println("Group Identifier Value :" + identifier.hasValue());
            }
            if (identifier.hasPeriod()) {
                Period period = identifier.getPeriod();
                if (period.hasStart()) {
                    System.out.println("Period Start:" + period.getStart());
                }
                if (period.hasEnd()) {
                    System.out.println("Period End:" + period.getEnd());
                }
            }
            if (identifier.hasAssigner()) {

                String assignerName = identifier.getAssigner().getDisplay();
                System.out.println("Assigner Name: " + assignerName);
            }
        }
        //status
        MedicationRequest.MedicationRequestStatus status =medicationRequest.getStatus();
        System.out.println("Medication Request Status:"+ status);
        //status reason
        if(medicationRequest.hasStatusReason()){
            CodeableConcept statusReason = medicationRequest.getStatusReason();
            if (statusReason.hasText()) {
                System.out.println("Status Reason Text :" + statusReason.getText());
            }
            if (statusReason .hasCoding()) {
                List<Coding> codes =statusReason.getCoding();
                for (Coding code : codes) {
                    if (code.hasSystem()) {
                        System.out.println("Code System :" + code.getSystem());
                    }
                    if (code.hasCode()) {
                        System.out.println("Code:" + code.getCode());
                    }
                    if (code.hasVersion()) {
                        System.out.println("Code element:" + code.getVersion());
                    }
                    if (code.hasDisplay()) {
                        System.out.println("Code Display:" + code.getDisplay());
                    }

                }

            }
        }
        //intent
        if(medicationRequest.hasIntent()){
            System.out.println("Medication Request:"+medicationRequest.getIntent());
        }
        //category
        if(medicationRequest.hasCategory()){
            List<CodeableConcept> categories = medicationRequest.getCategory();
            for(CodeableConcept category:categories){
                if (category.hasText()) {
                    System.out.println("category Text :" + category.getText());
                }
                if (category.hasCoding()) {
                    List<Coding> codes = category.getCoding();
                    for (Coding code : codes) {
                        if (code.hasSystem()) {
                            System.out.println("Code System" + code.getSystem());
                        }
                        if (code.hasCode()) {
                            System.out.println("Code:" + code.getCode());
                        }
                        if (code.hasVersion()) {
                            System.out.println("Code element:" + code.getVersion());
                        }
                        if (code.hasDisplay()) {
                            System.out.println("Code Display:" + code.getDisplay());
                        }

                    }

                }
            }
        }
        //priority
        if(medicationRequest.hasPriority()){
            MedicationRequest.MedicationRequestPriority priority = medicationRequest.getPriority();
            System.out.println("Priority :"+ medicationRequest.getPriority());
        }
        //
        return FhirJson;
    }

    public String MedicationAdministrationData(String FhirJson) {
        return FhirJson;
    }

    public String MedicationStatementData(String FhirJson) {
        return FhirJson;
    }

    public String MedicationDispenseData(String FhirJson) {
        return FhirJson;
    }

    public String Medication(String FhirJson) {
        return FhirJson;
    }
}
