package com.example.demo.FHIRJSONStructures;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
                System.out.println("Group Identifier Use : " + use);
            }
            if (identifier.hasType()) {
                CodeableConcept type = identifier.getType();
                String TypeText = type.getText();
                System.out.println("Group Identifier Text :" + TypeText);
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
        MedicationRequest.MedicationRequestStatus status = medicationRequest.getStatus();
        System.out.println("Medication Request Status:" + status);
        //status reason
        if (medicationRequest.hasStatusReason()) {
            CodeableConcept statusReason = medicationRequest.getStatusReason();
            if (statusReason.hasText()) {
                System.out.println("Status Reason Text :" + statusReason.getText());
            }
            if (statusReason.hasCoding()) {
                List<Coding> codes = statusReason.getCoding();
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
        if (medicationRequest.hasIntent()) {
            System.out.println("Medication Request:" + medicationRequest.getIntent());
        }
        //category
        if (medicationRequest.hasCategory()) {
            List<CodeableConcept> categories = medicationRequest.getCategory();
            for (CodeableConcept category : categories) {
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
        if (medicationRequest.hasPriority()) {
            MedicationRequest.MedicationRequestPriority priority = medicationRequest.getPriority();
            System.out.println("Priority :" + medicationRequest.getPriority());
        }
        //do not perform
        if (medicationRequest.hasDoNotPerform()) {
            System.out.println("Do not Perform Medication :" + medicationRequest.getDoNotPerform());
        }
        //medication
        Reference medication = medicationRequest.getMedicationReference();
        CodeableConcept medicationRef = medicationRequest.getMedicationCodeableConcept();
        String TypeText = medicationRef.getText();

//Multiple coding system could be present for the system
        for (Coding coding : medicationRef.getCoding()) {
            System.out.println("Coding System: " + coding.getSystem());
            System.out.println("Coding Code: " + coding.getCode());
            System.out.println("Coding Display: " + coding.getDisplay());
        }

        // subject
        Reference subject = medicationRequest.getSubject();
        System.out.println("Subject:" + subject.getReference());
        System.out.println("Subject display:" + subject.getDisplay());

        //information source
        if (medicationRequest.hasSupportingInformation()) {
            List<Reference> references = medicationRequest.getSupportingInformation();
            for (Reference supportingInfo : references) {
                System.out.println("Supporting Info:" + supportingInfo.getReference());
                System.out.println("SupportingInfo display:" + supportingInfo.getDisplay());
            }

        }
        //Encounter
        if (medicationRequest.hasEncounter()) {
            Reference encounter = medicationRequest.getEncounter();
            System.out.println("Encounter Info:" + encounter.getReference());
            System.out.println("Encounter display:" + encounter.getDisplay());
        }
        //authoredOn
        if (medicationRequest.hasAuthoredOn()) {
            System.out.println("Authored ON:" + medicationRequest.getAuthoredOn());
        }
        // requester
        if (medicationRequest.hasRequester()) {
            Reference requester = medicationRequest.getRequester();
            System.out.println("Requester Info:" + requester.getReference());
            System.out.println("Requester display:" + requester.getDisplay());
        }
        //reported
        if (medicationRequest.hasReported()) {
            System.out.println("Reported :" + medicationRequest.getReported());
        }
        // performerType
        if (medicationRequest.hasPerformerType()) {
            CodeableConcept performerType = medicationRequest.getPerformerType();
            String performerText = performerType.getText();
            System.out.println("Text:" + performerText);
//Multiple coding system could be present for the system
            for (Coding coding : performerType.getCoding()) {
                System.out.println("Coding System: " + coding.getSystem());
                System.out.println("Coding Code: " + coding.getCode());
                System.out.println("Coding Display: " + coding.getDisplay());
            }
        }
        //performer
        if (medicationRequest.hasPerformer()) {
            Reference performer = medicationRequest.getPerformer();
            System.out.println("Requester Info:" + performer.getReference());
            System.out.println("Requester display:" + performer.getDisplay());
        }
        //Course Therapy Type
        if (medicationRequest.hasCourseOfTherapyType()) {
            CodeableConcept therapyType = medicationRequest.getCourseOfTherapyType();
            String therapyText = therapyType.getText();
            System.out.println("Therapy Type : " + therapyText);
//Multiple coding system could be present for the system
            for (Coding coding : therapyType.getCoding()) {
                System.out.println("Coding System: " + coding.getSystem());
                System.out.println("Coding Code: " + coding.getCode());
                System.out.println("Coding Display: " + coding.getDisplay());
            }
        }

        // note
        if (medicationRequest.hasNote()) {
            List<Annotation> notes = medicationRequest.getNote();
            for (Annotation note : notes) {
                System.out.println("Note" + note);
            }

        }
        //DosageInstruction
        if (medicationRequest.hasDosageInstruction()) {
            List<Dosage> dosages = medicationRequest.getDosageInstruction();
            for (Dosage dosage : dosages) {
                if (dosage.hasDoseAndRate()) {
                    List<Dosage.DosageDoseAndRateComponent> doses = dosage.getDoseAndRate();
                    for (Dosage.DosageDoseAndRateComponent dose : doses) {
                        if (dose.hasType()) {
                            CodeableConcept type = dose.getType();
                            String doseText = type.getText();
                            System.out.println("Dose Text: " + doseText);

                            for (Coding coding : type.getCoding()) {
                                System.out.println("Coding System: " + coding.getSystem());
                                System.out.println("Coding Code: " + coding.getCode());
                                System.out.println("Coding Display: " + coding.getDisplay());
                            }
                        }
                        if (dose.hasDose()) {
                            if (dose.hasDoseRange()) {
                                Range doseRange = dose.getDoseRange();
                                System.out.println("Found Dose Range: "
                                        + doseRange.getLow().getValue() + " to "
                                        + doseRange.getHigh().getValue() + " "
                                        + doseRange.getLow().getUnit());
                            }
                            if (dose.hasDoseQuantity()) {
                                Quantity doseQuantity = dose.getDoseQuantity();
                                System.out.println("Found Dose Quantity: "
                                        + doseQuantity.getValue() + " "
                                        + doseQuantity.getUnit());
                            }

                        }
                        if (dose.hasRate()) {
                            if (dose.hasRateRatio()) {
                                Ratio rateRatio = dose.getRateRatio();
                                System.out.println("Found Rate Ratio: "
                                        + rateRatio.getNumerator().getValue() + " " + rateRatio.getNumerator().getUnit()
                                        + " / "
                                        + rateRatio.getDenominator().getValue() + " " + rateRatio.getDenominator().getUnit());
                            }
                            if (dose.hasRateQuantity()) {
                                Quantity rateQuantity = dose.getRateQuantity();
                                System.out.println("Found Rate Quantity: "
                                        + rateQuantity.getValue() + " " + rateQuantity.getUnit());
                            }
                            if (dose.hasRateRange()) {
                                Range rateRange = dose.getRateRange();
                                System.out.println("Found Rate Range: "
                                        + rateRange.getLow().getValue() + " to "
                                        + rateRange.getHigh().getValue() + " "
                                        + rateRange.getLow().getUnit());
                            }
                        }
                    }

                }
                if (dosage.hasSequence()) {
                    System.out.println("Dose Sequence :" + dosage.getSequence());
                }
                if (dosage.hasText()) {
                    System.out.println("Dose Text :" + dosage.getText());
                }
                if (dosage.hasAdditionalInstruction()) {
                    List<CodeableConcept> additionalInstructions = dosage.getAdditionalInstruction();
                    for (CodeableConcept Instruction : additionalInstructions) {
                        String doseText = Instruction.getText();
                        System.out.println("Dose Text: " + doseText);

                        for (Coding coding : Instruction.getCoding()) {
                            System.out.println("Coding System: " + coding.getSystem());
                            System.out.println("Coding Code: " + coding.getCode());
                            System.out.println("Coding Display: " + coding.getDisplay());
                        }
                    }
                }
                if (dosage.hasPatientInstruction()) {
                    System.out.println("Patient Instruction :" + dosage.getPatientInstruction());
                }
                if (dosage.hasTiming()) {
                    Timing timing = dosage.getTiming();

                    if (timing.hasEvent()) {
                        System.out.println("Found specific timing events:");
                        for (DateTimeType event : timing.getEvent()) {
                            System.out.println("- " + event.getValueAsString());
                        }
                    }

                }
                if (dosage.hasAsNeeded()) {
                    System.out.println("As Needed: " + dosage.getAsNeededBooleanType());
                    CodeableConcept asNeeded = dosage.getAsNeededCodeableConcept();
                    String asNeededText = asNeeded.getText();
                    System.out.println("Dose Text: " + asNeededText);

                    for (Coding coding : asNeeded.getCoding()) {
                        System.out.println("Coding System: " + coding.getSystem());
                        System.out.println("Coding Code: " + coding.getCode());
                        System.out.println("Coding Display: " + coding.getDisplay());
                    }

                }
                if (dosage.hasSite()) {
                    CodeableConcept site = dosage.getSite();
                    String siteText = site.getText();
                    System.out.println("Dose Text: " + siteText);

                    for (Coding coding : site.getCoding()) {
                        System.out.println("Coding System: " + coding.getSystem());
                        System.out.println("Coding Code: " + coding.getCode());
                        System.out.println("Coding Display: " + coding.getDisplay());
                    }
                }
                if (dosage.hasRoute()) {
                    CodeableConcept route = dosage.getRoute();
                    String routeText = route.getText();
                    System.out.println("Dose Text: " + routeText);

                    for (Coding coding : route.getCoding()) {
                        System.out.println("Coding System: " + coding.getSystem());
                        System.out.println("Coding Code: " + coding.getCode());
                        System.out.println("Coding Display: " + coding.getDisplay());
                    }
                }
                if (dosage.hasMethod()) {
                    CodeableConcept method = dosage.getMethod();
                    String methodText = method.getText();
                    System.out.println("Method Text: " + methodText);

                    for (Coding coding : method.getCoding()) {
                        System.out.println("Coding System: " + coding.getSystem());
                        System.out.println("Coding Code: " + coding.getCode());
                        System.out.println("Coding Display: " + coding.getDisplay());
                    }
                }
                if (dosage.hasMaxDosePerAdministration()) {
                    Quantity doseQuantity = dosage.getMaxDosePerAdministration();
                    System.out.println("Max Dose Per Administration Quantity: "
                            + doseQuantity.getValue() + " "
                            + doseQuantity.getUnit());
                }
                if (dosage.hasMaxDosePerPeriod()) {
                    Ratio maxDosePerPeriod = dosage.getMaxDosePerPeriod();


                    Quantity numerator = maxDosePerPeriod.getNumerator();
                    Quantity denominator = maxDosePerPeriod.getDenominator();
                    if (numerator.hasValue() && numerator.hasUnit()) {
                        System.out.println("  Numerator: " + numerator.getValue() + " " + numerator.getUnit());
                    }
                    if (denominator.hasValue() && denominator.hasUnit()) {
                        System.out.println("  Denominator: " + denominator.getValue() + " " + denominator.getUnit());
                    }
                }
                if (dosage.hasMaxDosePerLifetime()) {
                    Quantity doseQuantity = dosage.getMaxDosePerLifetime();
                    System.out.println("Max Dose Per Lifetime Quantity: "
                            + doseQuantity.getValue() + " "
                            + doseQuantity.getUnit());
                }
            }

        }

        return FhirJson;
    }

    public String Medication(String FhirJson) {
        if(FhirJson.contains("\"doseForm\"")){
            FhirJson = FhirJson.replace("\"doseForm\"","\"form\"");
        }
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        Medication medication = parser.parseResource(Medication.class, FhirJson);
        if (medication.hasIdentifier()) {
            List<Identifier> identifiers = medication.getIdentifier();

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
        if (medication.hasCode()) {

            CodeableConcept medicationCode = medication.getCode();
            if (medicationCode.hasText()) {
                System.out.println("MedicationCode Text :" + medicationCode.getText());
            }
            if (medicationCode.hasCoding()) {
                List<Coding> codes = medicationCode.getCoding();
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
        if (medication.hasStatus()) {
            System.out.println("Medication Status" + medication.getStatus());
        }
        //dose form
        if (medication.hasForm()) {
            CodeableConcept doseForm = medication.getForm();
            System.out.println("Dose Form: "+doseForm.getCoding());
            if (doseForm.hasText()) {
                System.out.println("Medication dose Code Text :" + doseForm.getText());
            }
            if (doseForm.hasCoding()) {
                List<Coding> codes = doseForm.getCoding();
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
        //totalVolume
        if (medication.hasAmount()) {

            Ratio doseQuantity = medication.getAmount();
            Quantity numerator = doseQuantity.getNumerator();
            Quantity denominator = doseQuantity.getDenominator();
            if (numerator.hasValue() && numerator.hasUnit()) {
                System.out.println("Numerator: " + numerator.getValue() + " " + numerator.getUnit());
            }
            if (denominator.hasValue() && denominator.hasUnit()) {
                System.out.println("Denominator: " + denominator.getValue() + " " + denominator.getUnit());
            }
        }
        //Ingredient
        System.out.println("Ingredient"+medication.hasIngredient());
//        if (medication.hasIngredient()) {
            List<Medication.MedicationIngredientComponent> ingredients = medication.getIngredient();
            for (Medication.MedicationIngredientComponent ingredient : ingredients) {
                System.out.println("--- Found Ingredient ---");
                if (ingredient.hasItem()) {

                    if (ingredient.hasItemCodeableConcept()) {
                        CodeableConcept item = ingredient.getItemCodeableConcept();
                        if (item.hasCoding()) {
                            System.out.println("Ingredient Code: " + item.getCodingFirstRep().getCode());
                            System.out.println("Ingredient Display: " + item.getCodingFirstRep().getDisplay());
                        }
                    }
                    // Check if the item is a Reference (for more complex or separately defined substances)
                    else if (ingredient.hasItemReference()) {
                        Reference itemRef = ingredient.getItemReference();
                        System.out.println("Ingredient Reference: " + itemRef.getReference());
                        if (itemRef.hasDisplay()) {
                            System.out.println("Ingredient Display: " + itemRef.getDisplay());
                        }
                    }
                }

                if (ingredient.hasStrength()) {
                    if (ingredient.getStrength() instanceof Ratio) {
                        Ratio strengthRatio = (Ratio) ingredient.getStrength();
                        System.out.println("Strength: "
                                + strengthRatio.getNumerator().getValue() + " " + strengthRatio.getNumerator().getUnit()
                                + " per "
                                + strengthRatio.getDenominator().getValue() + " " + strengthRatio.getDenominator().getUnit());
                    }

                }
            }
//        }
        //Batch
        if (medication.hasBatch()) {
            Medication.MedicationBatchComponent batch = medication.getBatch();
            if (batch.hasLotNumber()) {
                System.out.println("Lot Number: " + batch.getLotNumber());
            }
            if (batch.hasExpirationDate()) {
                Date expirationDate = batch.getExpirationDate();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                System.out.println("Expiration Date: " + dateFormat.format(expirationDate));
            }
        }
        return FhirJson;
    }

    public String MedicationAdministrationData(String FhirJson) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        MedicationAdministration medicationAdministration = parser.parseResource(MedicationAdministration.class, FhirJson);
        if (medicationAdministration.hasIdentifier()) {
            List<Identifier> identifiers = medicationAdministration.getIdentifier();

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
        System.out.println("Status : " + medicationAdministration.getStatus());
        if (medicationAdministration.hasStatusReason()) {
            List<CodeableConcept> statusReasons = medicationAdministration.getStatusReason();
            for (CodeableConcept statusReason : statusReasons) {

                if (statusReason.hasText()) {
                    System.out.println("StatusReason Text :" + statusReason.getText());
                }
                if (statusReason.hasCoding()) {
                    List<Coding> codes = statusReason.getCoding();
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
        if (medicationAdministration.hasCategory()) {
            CodeableConcept category = medicationAdministration.getCategory();


            if (category.hasText()) {
                System.out.println("Category Text :" + category.getText());
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
        Type medication = medicationAdministration.getMedication();
        if (medication instanceof Reference medicationReference) {
            // The reference contains the URL pointing to the Medication resource
            String referenceUrl = medicationReference.getReference();
            System.out.println("Medication Reference URL: " + referenceUrl);
            System.out.println("Medication Reference Display: " + medicationReference.getDisplay());
        } else if (medication instanceof CodeableConcept medicationRef) {
            for (Coding coding : medicationRef.getCoding()) {
                System.out.println("Coding System: " + coding.getSystem());
                System.out.println("Coding Code: " + coding.getCode());
                System.out.println("Coding Display: " + coding.getDisplay());
            }
            String code = medicationRef.getCodingFirstRep().getCode();
            String display = medicationRef.getCodingFirstRep().getDisplay();
            System.out.println("Medication Code: " + code);
            System.out.println("Medication Display: " + display);
        }

//Multiple coding system could be present for the system


        // subject
        Reference subject = medicationAdministration.getSubject();
        System.out.println("Subject:" + subject.getReference());
        System.out.println("Subject display:" + subject.getDisplay());
//Effective
        if (medicationAdministration.hasEffectiveDateTimeType()) {
            DateTimeType dateTimeType = medicationAdministration.getEffectiveDateTimeType();
            Date date = dateTimeType.getValue();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            System.out.println("Occurrence (dateTime): " + dateFormat.format(date));
        } else if (medicationAdministration.hasEffectivePeriod()) {
            Period period = medicationAdministration.getEffectivePeriod();
            Date start = period.getStart();
            Date end = period.getEnd();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            System.out.println("Occurrence (Period) start: " + dateFormat.format(start));
            System.out.println("Occurrence (Period) end: " + dateFormat.format(end));
        }

        return FhirJson;
    }

    public String MedicationStatementData(String FhirJson) {
        return FhirJson;
    }

    public String MedicationDispenseData(String FhirJson) {
        return FhirJson;
    }


}
