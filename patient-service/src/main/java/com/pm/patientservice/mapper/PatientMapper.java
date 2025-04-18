package com.pm.patientservice.mapper;

import com.pm.patientservice.dto.*;
import com.pm.patientservice.model.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Patient DTOs and entities
 */
@Component
public class PatientMapper {

    /**
     * Convert Patient entity to PatientResponse DTO
     */
    public PatientResponse toPatientResponse(Patient patient) {
        if (patient == null) {
            return null;
        }
        
        return new PatientResponse(
                patient.getId(),
                patient.getMedicalRecordNumber(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getGender(),
                toAddressDTO(patient.getAddress()),
                toContactInfoDTO(patient.getContactInfo()),
                toEmergencyContactDTOList(patient.getEmergencyContacts()),
                toInsuranceInfoDTO(patient.getInsuranceInfo()),
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }
    
    /**
     * Convert PatientCreateRequest DTO to Patient entity
     */
    public Patient toPatient(PatientCreateRequest request) {
        if (request == null) {
            return null;
        }
        
        Patient patient = new Patient();
        patient.setMedicalRecordNumber(request.getMedicalRecordNumber());
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setAddress(toAddress(request.getAddress()));
        patient.setContactInfo(toContactInfo(request.getContactInfo()));
        patient.setEmergencyContacts(toEmergencyContactList(request.getEmergencyContacts()));
        patient.setInsuranceInfo(toInsuranceInfo(request.getInsuranceInfo()));
        
        // Set creation and update timestamps
        Date now = new Date();
        patient.setCreatedAt(now);
        patient.setUpdatedAt(now);
        
        return patient;
    }
    
    /**
     * Update Patient entity from PatientUpdateRequest DTO
     */
    public void updatePatientFromDTO(PatientUpdateRequest request, Patient patient) {
        if (request == null || patient == null) {
            return;
        }
        
        patient.setMedicalRecordNumber(request.getMedicalRecordNumber());
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setAddress(toAddress(request.getAddress()));
        patient.setContactInfo(toContactInfo(request.getContactInfo()));
        patient.setEmergencyContacts(toEmergencyContactList(request.getEmergencyContacts()));
        patient.setInsuranceInfo(toInsuranceInfo(request.getInsuranceInfo()));
        
        // Update the update timestamp
        patient.setUpdatedAt(new Date());
    }
    
    // Helper methods for converting between DTOs and entities
    
    private AddressDTO toAddressDTO(Address address) {
        if (address == null) {
            return null;
        }
        return new AddressDTO(
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getZipCode(),
                address.getCountry()
        );
    }
    
    private Address toAddress(AddressDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Address(
                dto.getStreet(),
                dto.getCity(),
                dto.getState(),
                dto.getZipCode(),
                dto.getCountry()
        );
    }
    
    private ContactInfoDTO toContactInfoDTO(ContactInfo contactInfo) {
        if (contactInfo == null) {
            return null;
        }
        return new ContactInfoDTO(
                contactInfo.getPhoneNumber(),
                contactInfo.getEmail(),
                contactInfo.getAlternativePhoneNumber()
        );
    }
    
    private ContactInfo toContactInfo(ContactInfoDTO dto) {
        if (dto == null) {
            return null;
        }
        return new ContactInfo(
                dto.getPhoneNumber(),
                dto.getEmail(),
                dto.getAlternativePhoneNumber()
        );
    }
    
    private EmergencyContactDTO toEmergencyContactDTO(EmergencyContact contact) {
        if (contact == null) {
            return null;
        }
        return new EmergencyContactDTO(
                contact.getName(),
                contact.getRelationship(),
                contact.getPhoneNumber(),
                contact.getEmail(),
                toAddressDTO(contact.getAddress())
        );
    }
    
    private EmergencyContact toEmergencyContact(EmergencyContactDTO dto) {
        if (dto == null) {
            return null;
        }
        return new EmergencyContact(
                dto.getName(),
                dto.getRelationship(),
                dto.getPhoneNumber(),
                dto.getEmail(),
                toAddress(dto.getAddress())
        );
    }
    
    private List<EmergencyContactDTO> toEmergencyContactDTOList(List<EmergencyContact> contacts) {
        if (contacts == null) {
            return null;
        }
        return contacts.stream()
                .map(this::toEmergencyContactDTO)
                .collect(Collectors.toList());
    }
    
    private List<EmergencyContact> toEmergencyContactList(List<EmergencyContactDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::toEmergencyContact)
                .collect(Collectors.toList());
    }
    
    private InsuranceInfoDTO toInsuranceInfoDTO(InsuranceInfo info) {
        if (info == null) {
            return null;
        }
        return new InsuranceInfoDTO(
                info.getProvider(),
                info.getPolicyNumber(),
                info.getGroupNumber(),
                info.getPolicyHolderName(),
                info.getEffectiveDate(),
                info.getExpirationDate(),
                info.getCoverageType()
        );
    }
    
    private InsuranceInfo toInsuranceInfo(InsuranceInfoDTO dto) {
        if (dto == null) {
            return null;
        }
        return new InsuranceInfo(
                dto.getProvider(),
                dto.getPolicyNumber(),
                dto.getGroupNumber(),
                dto.getPolicyHolderName(),
                dto.getEffectiveDate(),
                dto.getExpirationDate(),
                dto.getCoverageType()
        );
    }
}