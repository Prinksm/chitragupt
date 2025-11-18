package com.example.demo.services.fhirBuilder;

import ca.uhn.fhir.context.FhirContext;
import com.example.demo.entity.patientEntity.ContactAddress;
import com.example.demo.entity.patientEntity.ContactTelecom;
import com.example.demo.entity.patientEntity.Patient;
import com.example.demo.entity.patientEntity.PatientContact;
import com.example.demo.entity.userEntity.CommonAddress;
import com.example.demo.entity.userEntity.CommonTelecom;
import com.example.demo.repository.CommonAddressRepository;
import com.example.demo.repository.CommonTelecomRepository;
import com.example.demo.repository.Patient.ContactAddressRepository;
import com.example.demo.repository.Patient.ContactTelecomRepository;
import com.example.demo.repository.Patient.PatientContactRepository;
import com.example.demo.repository.Patient.PatientRepository;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Patient.ContactComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class PatientFhirBuilderService {

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private PatientContactRepository patientContactRepo;

    @Autowired
    private ContactAddressRepository contactAddressRepository;

    @Autowired
    private ContactTelecomRepository contactTelecomRepository;

    @Autowired
    private CommonAddressRepository commonAddressRepo;

    @Autowired
    private CommonTelecomRepository commonTelecomRepo;

    @Transactional
    public void buildPatientFhirJson() {
        List<Patient> patientsWithoutFhir = patientRepo.findByFhirIsNull();
        for (Patient patientEntity : patientsWithoutFhir) {
            org.hl7.fhir.r4.model.Patient fhirPatient = buildPatientFhir(patientEntity);
            FhirContext ctx = FhirContext.forR4();
            String fhirJson = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(fhirPatient);
            System.out.println(fhirJson);
            patientEntity.setFhir(fhirJson);
            patientRepo.save(patientEntity);
        }
    }

    public org.hl7.fhir.r4.model.Patient buildPatientFhir(Patient patientEntity) {
        org.hl7.fhir.r4.model.Patient fhirPatient = new org.hl7.fhir.r4.model.Patient();
        fhirPatient.setId(String.valueOf(patientEntity.getId()));

        // Add name
        if (patientEntity.getFirstName() != null || patientEntity.getLastName() != null) {
            HumanName name = new HumanName();
            if (patientEntity.getLastName() != null) {
                name.setFamily(patientEntity.getLastName());
            }
            if (patientEntity.getFirstName() != null) {
                name.addGiven(patientEntity.getFirstName());
            }
            if (patientEntity.getMiddleName() != null) {
                name.addGiven(patientEntity.getMiddleName());
            }
            fhirPatient.addName(name);
        }

        // Add gender
        if (patientEntity.getGender() != null) {
            try {
                fhirPatient.setGender(Enumerations.AdministrativeGender.fromCode(patientEntity.getGender().toLowerCase()));
            } catch (Exception e) {
                fhirPatient.setGender(Enumerations.AdministrativeGender.UNKNOWN);
            }
        }

        // Add birth date
        if (patientEntity.getBirthDate() != null) {
            Date birthDate = convertLocalDateToDate(patientEntity.getBirthDate());
            fhirPatient.setBirthDate(birthDate);
        }

        // Add contacts
        List<PatientContact> contacts = patientContactRepo.findByPatient_Id(patientEntity.getId());
        for (PatientContact contact : contacts) {
            ContactComponent fhirContact = new ContactComponent();

            // Set relationship
            if (contact.getRelationshipType() != null) {
                CodeableConcept relationship = new CodeableConcept();
                Coding coding = relationship.addCoding();
                coding.setSystem("http://terminology.hl7.org/CodeSystem/v2-0131");
                coding.setCode(contact.getRelationshipType());
                coding.setDisplay(contact.getRelationshipType());
                fhirContact.setRelationship(Collections.singletonList(relationship));
            }

            // Set name
            if (contact.getFirstName() != null || contact.getLastName() != null) {
                HumanName contactName = new HumanName();
                if (contact.getLastName() != null) {
                    contactName.setFamily(contact.getLastName());
                }
                if (contact.getFirstName() != null) {
                    contactName.addGiven(contact.getFirstName());
                }
                if (contact.getMiddleName() != null) {
                    contactName.addGiven(contact.getMiddleName());
                }
                fhirContact.setName(contactName);
            }

            // Fetch and add telecoms for this contact
            List<ContactTelecom> contactTelecoms = contactTelecomRepository.findByContactId(contact.getId());
            for (ContactTelecom contactTelecom : contactTelecoms) {
                Optional<CommonTelecom> telecom = commonTelecomRepo.findById(contactTelecom.getId());
                telecom.ifPresent(t -> {
                    ContactPoint contactPoint = new ContactPoint();
                    if (t.getSystem() != null) {
                        contactPoint.setSystem(ContactPoint.ContactPointSystem.fromCode(t.getSystem()));
                    }
                    if (t.getValue() != null) {
                        contactPoint.setValue(t.getValue());
                    }
                    fhirContact.addTelecom(contactPoint);
                });
            }
            fhirPatient.addContact(fhirContact);

            // Fetch and add addresses for this contact
            List<ContactAddress> contactAddresses = contactAddressRepository.findByContactId(contact.getId());
            List<Address> addresses = new ArrayList<>();
            for (ContactAddress contactAddress : contactAddresses) {
                Optional<CommonAddress> address = commonAddressRepo.findById(contactAddress.getId());
                address.ifPresent(a -> {
                    Address addr = new Address();
                    if (a.getUseCode() != null) {
                        addr.setUse(Address.AddressUse.fromCode(a.getUseCode()));
                    }
                    if (a.getAddressType() != null) {
                        addr.setType(Address.AddressType.fromCode(a.getAddressType()));
                    }
                    if (a.getText() != null) {
                        addr.setText(a.getText());
                    }
                    if (a.getLine1() != null) {
                        addr.addLine(a.getLine1());
                    }
                    if (a.getLine2() != null) {
                        addr.addLine(a.getLine2());
                    }
                    if (a.getCity() != null) {
                        addr.setCity(a.getCity());
                    }
                    if (a.getDistrict() != null) {
                        addr.setDistrict(a.getDistrict());
                    }
                    if (a.getState() != null) {
                        addr.setState(a.getState());
                    }
                    if (a.getPostalCode() != null) {
                        addr.setPostalCode(a.getPostalCode());
                    }
                    if (a.getCountry() != null) {
                        addr.setCountry(a.getCountry());
                    }
                    addresses.add(addr); // Add the address to the list
                });
            }

            addresses.stream().forEach(address -> fhirContact.setAddress(address));

//            fhirContact.setAddress(addresses); // Set the list of addresses to the contact
        }


            // Add telecoms
        List<CommonTelecom> telecoms = commonTelecomRepo.findByPatientId(patientEntity.getId());
        for (CommonTelecom telecom : telecoms) {
            ContactPoint contactPoint = new ContactPoint();
            if (telecom.getSystem() != null) {
                contactPoint.setSystem(ContactPoint.ContactPointSystem.fromCode(telecom.getSystem()));
            }
            if (telecom.getValue() != null) {
                contactPoint.setValue(telecom.getValue());
            }
            fhirPatient.addTelecom(contactPoint);
        }

        // Add addresses
        List<CommonAddress> addresses = commonAddressRepo.findByPatientId(patientEntity.getId());
        for (CommonAddress address : addresses) {
            Address addr = new Address();
            if (address.getUseCode() != null) {
                addr.setUse(Address.AddressUse.fromCode(address.getUseCode()));
            }
            if (address.getAddressType() != null) {
                addr.setType(Address.AddressType.fromCode(address.getAddressType()));
            }
            if (address.getText() != null) {
                addr.setText(address.getText());
            }
            if (address.getLine1() != null) {
                addr.addLine(address.getLine1());
            }
            if (address.getLine2() != null) {
                addr.addLine(address.getLine2());
            }
            if (address.getCity() != null) {
                addr.setCity(address.getCity());
            }
            if (address.getDistrict() != null) {
                addr.setDistrict(address.getDistrict());
            }
            if (address.getState() != null) {
                addr.setState(address.getState());
            }
            if (address.getPostalCode() != null) {
                addr.setPostalCode(address.getPostalCode());
            }
            if (address.getCountry() != null) {
                addr.setCountry(address.getCountry());
            }
            fhirPatient.addAddress(addr);
        }

        return fhirPatient;
    }

    // Utility method to convert LocalDate to Date
    private Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
