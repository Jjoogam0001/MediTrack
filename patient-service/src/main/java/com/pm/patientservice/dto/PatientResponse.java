package com.pm.patientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * DTO for Patient response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {
    private String id;
    private String medicalRecordNumber;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String gender;
    private AddressDTO address;
    private ContactInfoDTO contactInfo;
    private List<EmergencyContactDTO> emergencyContacts;
    private InsuranceInfoDTO insuranceInfo;
    private Date createdAt;
    private Date updatedAt;
}