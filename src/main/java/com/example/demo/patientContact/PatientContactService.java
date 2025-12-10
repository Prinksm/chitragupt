package com.example.demo.patientContact;
import com.example.demo.emails.template.EmailTemplates;
import com.example.demo.patientContact.dto.ContactAddressDto;
import com.example.demo.patientContact.dto.ContactTelecomDto;
import com.example.demo.patientContact.dto.PatientContactDto;
import com.example.demo.patientContact.repository.ContactAddressRepository;
import com.example.demo.patientContact.repository.ContactTelecomRepository;
import com.example.demo.patientContact.repository.PatientContactRepository;
import com.example.demo.entity.patientEntity.ContactAddress;
import com.example.demo.entity.patientEntity.ContactTelecom;
import com.example.demo.entity.patientEntity.PatientContact;
import com.example.demo.entity.userEntity.CommonAddress;
import com.example.demo.entity.userEntity.CommonTelecom;
import com.example.demo.repository.AuthCommon.CommonAddressRepository;
import com.example.demo.repository.AuthCommon.CommonTelecomRepository;
import com.example.demo.profile.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientContactService {
    private final CommonTelecomRepository commonTelecomRepository;
    private final CommonAddressRepository commonAddressRepository;
    private final PatientContactRepository patientContactRepository;
    private final PatientRepository patientRepository;
    private final ContactTelecomRepository contactTelecomRepository;
    private final ContactAddressRepository contactAddressRepository;
    private  final EmailTemplates emailTemplates;


    private String extractPrimaryEmail(PatientContactDto dto) {
        if (dto.getContactTelecoms() == null || dto.getContactTelecoms().isEmpty()) {
            return null;
        }

        ContactTelecomDto firstTelecom = dto.getContactTelecoms().get(0);

        if (firstTelecom.getSystem() != null && firstTelecom.getSystem().equalsIgnoreCase("email")) {
            return firstTelecom.getValue();
        }

        return null; // Invalid ordering or no email found
    }


    //add
    @Transactional
    public PatientContactDto addPatientContact(PatientContactDto patientContactDto){
//        List<PatientContact>existingContact = patientContactRepository.findByPatient_Id(patientContactDto.getPatientId());
//        if(existingContact.isEmpty()){
//            if(patientContactDto.getPatientId() == null){
//                throw new IllegalArgumentException("Patient ID is required for the first contact.");
//            }
//        }
        PatientContact patientContact = new PatientContact();
        patientContact.setPatient(patientRepository.findById(patientContactDto.getPatientId()).orElseThrow());
        patientContact.setFirstName(patientContactDto.getFirstName());
        patientContact.setMiddleName(patientContactDto.getMiddleName());
        patientContact.setLastName(patientContactDto.getLastName());
        patientContact.setRelationshipType(patientContactDto.getRelationshipType());
        patientContact = patientContactRepository.save(patientContact);

        List<ContactTelecomDto> savedTelecomDTOs = new ArrayList<>();
        List<ContactAddressDto> savedAddressDTOs = new ArrayList<>();


        List<ContactTelecomDto> telecomDTOs = patientContactDto.getContactTelecoms();
        if (telecomDTOs != null) {
            for (ContactTelecomDto telecomDTO : telecomDTOs) {
                // Create and save CommonTelecom
                CommonTelecom commonTelecom = new CommonTelecom();
                commonTelecom.setSystem(telecomDTO.getSystem());
                commonTelecom.setValue(telecomDTO.getValue());
                commonTelecom = commonTelecomRepository.save(commonTelecom);

                // Create and save ContactTelecom
                ContactTelecom contactTelecom = new ContactTelecom();
                contactTelecom.setContact(patientContact);
                contactTelecom.setTelecom(commonTelecom);
                contactTelecomRepository.save(contactTelecom);

                ContactTelecomDto savedTelecomDTO = new ContactTelecomDto();
                savedTelecomDTO.setId(contactTelecom.getId());
                savedTelecomDTO.setSystem(commonTelecom.getSystem());
                savedTelecomDTO.setValue(commonTelecom.getValue());
                savedTelecomDTOs.add(savedTelecomDTO);
            }
        }

        // Save ContactAddresses
        List<ContactAddressDto> addressDTOs = patientContactDto.getContactAddresses();
        if (addressDTOs != null) {
            for (ContactAddressDto addressDTO : addressDTOs) {
                // Create and save CommonAddress
                CommonAddress commonAddress = new CommonAddress();
                commonAddress.setUseCode(addressDTO.getUseCode());
                commonAddress.setAddressType(addressDTO.getAddressType());
                commonAddress.setText(addressDTO.getText());
                commonAddress.setLine1(addressDTO.getLine1());
                commonAddress.setLine2(addressDTO.getLine2());
                commonAddress.setCity(addressDTO.getCity());
                commonAddress.setDistrict(addressDTO.getDistrict());
                commonAddress.setState(addressDTO.getState());
                commonAddress.setPostalCode(addressDTO.getPostalCode());
                commonAddress.setCountry(addressDTO.getCountry());
                commonAddress = commonAddressRepository.save(commonAddress);

                // Create and save ContactAddress
                ContactAddress contactAddress = new ContactAddress();
                contactAddress.setContact(patientContact);
                contactAddress.setAddress(commonAddress);
                contactAddressRepository.save(contactAddress);

                ContactAddressDto savedAddressDTO = new ContactAddressDto();
                savedAddressDTO.setId(contactAddress.getId());
                savedAddressDTO.setUseCode(commonAddress.getUseCode());
                savedAddressDTO.setAddressType(commonAddress.getAddressType());
                savedAddressDTO.setText(commonAddress.getText());
                savedAddressDTO.setLine1(commonAddress.getLine1());
                savedAddressDTO.setLine2(commonAddress.getLine2());
                savedAddressDTO.setCity(commonAddress.getCity());
                savedAddressDTO.setDistrict(commonAddress.getDistrict());
                savedAddressDTO.setState(commonAddress.getState());
                savedAddressDTO.setPostalCode(commonAddress.getPostalCode());
                savedAddressDTO.setCountry(commonAddress.getCountry());
                savedAddressDTOs.add(savedAddressDTO);

            }
        }
        patientContactDto.setId(patientContact.getId());
        patientContactDto.setContactTelecoms(savedTelecomDTOs);
        patientContactDto.setContactAddresses(savedAddressDTOs);
        try {
            String contactEmail = extractPrimaryEmail(patientContactDto);

            if (contactEmail != null) {
                String patientName = patientContact.getPatient().getFirstName() ;

                emailTemplates.sendEmergencyContactNotification(contactEmail, patientName);
            }
        } catch (Exception ex) {
            System.out.println("Failed to send emergency contact email: " + ex.getMessage());
        }


        return patientContactDto;
    }


    //update
    @Transactional
    public PatientContactDto updatePatientContact(Long id, PatientContactDto patientContactDto) {
        PatientContact patientContact = patientContactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PatientContact not found with id: " + id));

        String oldEmail = patientContact.getContactTelecoms().stream()
                .filter(t -> t.getTelecom().getSystem().equalsIgnoreCase("email"))
                .map(t -> t.getTelecom().getValue())
                .findFirst()
                .orElse(null);
        if (patientContactDto.getRemovedTelecomIds() != null) {
            for (Long telId : patientContactDto.getRemovedTelecomIds()) {
                // remove association from patientContact if present
                patientContact.getContactTelecoms().removeIf(ct -> telId.equals(ct.getId()));
                // delete from repo (this will remove the row)
                contactTelecomRepository.findById(telId).ifPresent(ct -> {
                    contactTelecomRepository.delete(ct);
                    // optionally delete the commonTelecom row if it's orphaned
                    CommonTelecom ctTele = ct.getTelecom();
                    if (ctTele != null) {
                        // you can add a check to see if any other ContactTelecom refers to it
                        long refs = contactTelecomRepository.countByTelecomId(ctTele.getId());
                        if (refs == 0) commonTelecomRepository.delete(ctTele);
                    }
                });
            }
        }

        // 2) Delete removed addresses (if provided)
        if (patientContactDto.getRemovedAddressIds() != null) {
            for (Long addrId : patientContactDto.getRemovedAddressIds()) {
                patientContact.getContactAddresses().removeIf(ca -> addrId.equals(ca.getId()));
                contactAddressRepository.findById(addrId).ifPresent(ca -> {
                    contactAddressRepository.delete(ca);
                    CommonAddress caddr = ca.getAddress();
                    if (caddr != null) {
                        long refs = contactAddressRepository.countByAddressId(caddr.getId());
                        if (refs == 0) commonAddressRepository.delete(caddr);
                    }
                });
            }
        }
        if (patientContactDto.getFirstName() != null) {
            patientContact.setFirstName(patientContactDto.getFirstName());
        }
        if (patientContactDto.getMiddleName() != null) {
            patientContact.setMiddleName(patientContactDto.getMiddleName());
        }
        if (patientContactDto.getLastName() != null) {
            patientContact.setLastName(patientContactDto.getLastName());
        }
        if (patientContactDto.getRelationshipType() != null) {
            patientContact.setRelationshipType(patientContactDto.getRelationshipType());
        }
        patientContact = patientContactRepository.save(patientContact);


        List<ContactTelecomDto> telecomDTOs = patientContactDto.getContactTelecoms();
        if (telecomDTOs != null) {

            for (ContactTelecomDto telecomDTO : telecomDTOs) {

                // CASE 1: Update existing telecom
                if (telecomDTO.getId() != null) {

                    ContactTelecom contactTelecom = contactTelecomRepository.findById(telecomDTO.getId())
                            .orElseThrow(() -> new RuntimeException("ContactTelecom not found: " + telecomDTO.getId()));

                    CommonTelecom commonTelecom = contactTelecom.getTelecom();

                    if (telecomDTO.getSystem() != null) commonTelecom.setSystem(telecomDTO.getSystem());
                    if (telecomDTO.getValue() != null) commonTelecom.setValue(telecomDTO.getValue());

                    patientContact.getContactTelecoms().add(contactTelecom);

                }
                // CASE 2: Add new telecom
                else {

                    CommonTelecom newTelecom = new CommonTelecom();
                    newTelecom.setSystem(telecomDTO.getSystem());
                    newTelecom.setValue(telecomDTO.getValue());
                    newTelecom = commonTelecomRepository.save(newTelecom);

                    ContactTelecom contactTelecom = new ContactTelecom();
                    contactTelecom.setTelecom(newTelecom);
                    contactTelecom.setContact(patientContact);

                    contactTelecomRepository.save(contactTelecom);
                    patientContact.getContactTelecoms().add(contactTelecom);
                }
            }
        }

        List<ContactAddressDto> addressDTOs = patientContactDto.getContactAddresses();
        if (addressDTOs != null) {

            for (ContactAddressDto addressDTO : addressDTOs) {

                // CASE 1: Update existing address
                if (addressDTO.getId() != null) {

                    ContactAddress contactAddress = contactAddressRepository.findById(addressDTO.getId())
                            .orElseThrow(() -> new RuntimeException("ContactAddress not found: " + addressDTO.getId()));

                    CommonAddress addr = contactAddress.getAddress();

                    if (addressDTO.getUseCode() != null) addr.setUseCode(addressDTO.getUseCode());
                    if (addressDTO.getAddressType() != null) addr.setAddressType(addressDTO.getAddressType());
                    if (addressDTO.getText() != null) addr.setText(addressDTO.getText());
                    if (addressDTO.getLine1() != null) addr.setLine1(addressDTO.getLine1());
                    if (addressDTO.getLine2() != null) addr.setLine2(addressDTO.getLine2());
                    if (addressDTO.getCity() != null) addr.setCity(addressDTO.getCity());
                    if (addressDTO.getDistrict() != null) addr.setDistrict(addressDTO.getDistrict());
                    if (addressDTO.getState() != null) addr.setState(addressDTO.getState());
                    if (addressDTO.getPostalCode() != null) addr.setPostalCode(addressDTO.getPostalCode());
                    if (addressDTO.getCountry() != null) addr.setCountry(addressDTO.getCountry());

                    patientContact.getContactAddresses().add(contactAddress);

                }
                // CASE 2: Add new address
                else {

                    CommonAddress addr = new CommonAddress();
                    addr.setUseCode(addressDTO.getUseCode());
                    addr.setAddressType(addressDTO.getAddressType());
                    addr.setText(addressDTO.getText());
                    addr.setLine1(addressDTO.getLine1());
                    addr.setLine2(addressDTO.getLine2());
                    addr.setCity(addressDTO.getCity());
                    addr.setDistrict(addressDTO.getDistrict());
                    addr.setState(addressDTO.getState());
                    addr.setPostalCode(addressDTO.getPostalCode());
                    addr.setCountry(addressDTO.getCountry());

                    addr = commonAddressRepository.save(addr);

                    ContactAddress contactAddress = new ContactAddress();
                    contactAddress.setAddress(addr);
                    contactAddress.setContact(patientContact);

                    contactAddressRepository.save(contactAddress);
                    patientContact.getContactAddresses().add(contactAddress);
                }
            }
        }

        patientContactRepository.save(patientContact);

        String newEmail = extractPrimaryEmail(patientContactDto);

        try {
            boolean emailUpdated = (newEmail != null && !newEmail.equals(oldEmail));

            if (emailUpdated) {
                String patientName = patientContact.getPatient().getFirstName();
                emailTemplates.sendEmergencyContactNotification(newEmail, patientName);
            }

        } catch (Exception ex) {
            System.out.println("Failed to send emergency contact email: " + ex.getMessage());
        }

        return mapPatientContactToDto(patientContact);
    }

    // Helper method to map PatientContact to DTO
    private PatientContactDto mapPatientContactToDto(PatientContact patientContact) {
        PatientContactDto dto = new PatientContactDto();
        dto.setId(patientContact.getId());
        dto.setPatientId(patientContact.getPatient().getId());
        dto.setFirstName(patientContact.getFirstName());
        dto.setMiddleName(patientContact.getMiddleName());
        dto.setLastName(patientContact.getLastName());
        dto.setRelationshipType(patientContact.getRelationshipType());

        // Map telecoms
        List<ContactTelecomDto> telecomDtos = new ArrayList<>();
        for (ContactTelecom contactTelecom : patientContact.getContactTelecoms()) {
//
            ContactTelecomDto telecomDto = new ContactTelecomDto();
            telecomDto.setId(contactTelecom.getId());
            telecomDto.setSystem(contactTelecom.getTelecom().getSystem());
            telecomDto.setValue(contactTelecom.getTelecom().getValue());
            telecomDtos.add(telecomDto);
        }
        dto.setContactTelecoms(telecomDtos);

        // Map addresses
        List<ContactAddressDto> addressDtos = new ArrayList<>();
        for (ContactAddress contactAddress : patientContact.getContactAddresses()) {
            ContactAddressDto addressDto = new ContactAddressDto();
            addressDto.setId(contactAddress.getId());
            addressDto.setUseCode(contactAddress.getAddress().getUseCode());
            addressDto.setAddressType(contactAddress.getAddress().getAddressType());
            addressDto.setText(contactAddress.getAddress().getText());
            addressDto.setLine1(contactAddress.getAddress().getLine1());
            addressDto.setLine2(contactAddress.getAddress().getLine2());
            addressDto.setCity(contactAddress.getAddress().getCity());
            addressDto.setDistrict(contactAddress.getAddress().getDistrict());
            addressDto.setState(contactAddress.getAddress().getState());
            addressDto.setPostalCode(contactAddress.getAddress().getPostalCode());
            addressDto.setCountry(contactAddress.getAddress().getCountry());
            addressDtos.add(addressDto);
        }
        dto.setContactAddresses(addressDtos);

        return dto;
    }



    //get
    @Transactional
    public List<PatientContactDto> getPatientContacts(Long patientId){
        List<PatientContact> patientContacts = patientContactRepository.findByPatient_Id(patientId);
        if (patientContacts.isEmpty()) {
            throw new RuntimeException("PatientContact not found with patient id: " + patientId);
        }
        List<PatientContactDto> patientContactDTOs = new ArrayList<>();
        for (PatientContact patientContact : patientContacts){
            PatientContactDto patientContactDTO = new PatientContactDto();
            patientContactDTO.setId(patientContact.getId());
            patientContactDTO.setPatientId(patientContact.getPatient().getId());
            patientContactDTO.setFirstName(patientContact.getFirstName());
            patientContactDTO.setMiddleName(patientContact.getMiddleName());
            patientContactDTO.setLastName(patientContact.getLastName());
            patientContactDTO.setRelationshipType(patientContact.getRelationshipType());

            List<ContactTelecom> contactTelecoms = contactTelecomRepository.findByContactId(patientContact.getId());
            List<ContactTelecomDto> telecomDTOs = new ArrayList<>();
            for (ContactTelecom contactTelecom : contactTelecoms) {
                Optional<CommonTelecom> commonTelecom = commonTelecomRepository.findById(contactTelecom.getTelecom().getId());
                commonTelecom.ifPresent(t -> {
                    ContactTelecomDto telecomDTO = new ContactTelecomDto();
                    telecomDTO.setId(contactTelecom.getId());
                    telecomDTO.setSystem(t.getSystem());
                    telecomDTO.setValue(t.getValue());
                    telecomDTOs.add(telecomDTO);
                });
            }
            patientContactDTO.setContactTelecoms(telecomDTOs);

            List<ContactAddress> contactAddresses = contactAddressRepository.findByContactId(patientContact.getId());
            List<ContactAddressDto> addressDTOs = new ArrayList<>();
            for (ContactAddress contactAddress : contactAddresses) {
                Optional<CommonAddress> commonAddress = commonAddressRepository.findById(contactAddress.getAddress().getId());
                commonAddress.ifPresent(a -> {
                    ContactAddressDto addressDTO = new ContactAddressDto();
                    addressDTO.setId(contactAddress.getId());
                    addressDTO.setUseCode(a.getUseCode());
                    addressDTO.setAddressType(a.getAddressType());
                    addressDTO.setText(a.getText());
                    addressDTO.setLine1(a.getLine1());
                    addressDTO.setLine2(a.getLine2());
                    addressDTO.setCity(a.getCity());
                    addressDTO.setDistrict(a.getDistrict());
                    addressDTO.setState(a.getState());
                    addressDTO.setPostalCode(a.getPostalCode());
                    addressDTO.setCountry(a.getCountry());
                    addressDTOs.add(addressDTO);
                });
            }
            patientContactDTO.setContactAddresses(addressDTOs);
            patientContactDTOs.add(patientContactDTO);
        }
        return patientContactDTOs;
    }


    //delete
    @Transactional
    //contact id
    /**
     * @Author Prinkal Mahajan
     * @return void
     * @paramaeter
     * @descri
     */
    public void deletePatientContact(Long id) {
        PatientContact patientContact = patientContactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PatientContact not found with id: " + id));


        List<PatientContact> existingContacts = patientContactRepository.findByPatient_Id(patientContact.getPatient().getId());
        if (existingContacts.size() <= 1) {
            throw new IllegalArgumentException("Cannot delete the last contact for a patient.");
        }

        List<ContactTelecom> contactTelecoms = contactTelecomRepository.findByContactId(id);

        List<Long> telecomIds = new ArrayList<>();
        for (ContactTelecom contactTelecom : contactTelecoms) {
            telecomIds.add(contactTelecom.getTelecom().getId());
        }

        contactTelecomRepository.deleteAll(contactTelecoms);

        for (Long telecomId : telecomIds) {
            Optional<ContactTelecom> remainingTelecoms = contactTelecomRepository.findById(telecomId);
            if (remainingTelecoms.isEmpty()) {
                commonTelecomRepository.deleteById(telecomId);
            }
        }

        List<ContactAddress> contactAddresses = contactAddressRepository.findByContactId(id);

        List<Long> addressIds = new ArrayList<>();
        for (ContactAddress contactAddress : contactAddresses) {
            addressIds.add(contactAddress.getAddress().getId());
        }

        contactAddressRepository.deleteAll(contactAddresses);

        for (Long addressId : addressIds) {
            Optional<ContactAddress> remainingAddresses = contactAddressRepository.findById(addressId);
            if (remainingAddresses.isEmpty()) {
                commonAddressRepository.deleteById(addressId);
            }
        }
        patientContactRepository.deleteById(id);
    }

}
