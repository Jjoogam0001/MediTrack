package com.pm.patientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.List;

/**
 * DTO for creating a new Patient
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientCreateRequest {
    @NotBlank(message = "Medical record number is required")
    @Size(min = 5, max = 50, message = "Medical record number must be between 5 and 50 characters")
    private String medicalRecordNumber;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must be less than 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must be less than 100 characters")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private Date dateOfBirth;

    @NotBlank(message = "Gender is required")
    private String gender;

    @Valid
    @NotNull(message = "Address is required")
    private AddressDTO address;

    @Valid
    @NotNull(message = "Contact information is required")
    private ContactInfoDTO contactInfo;

    @Valid
    private List<EmergencyContactDTO> emergencyContacts;

    @Valid
    private InsuranceInfoDTO insuranceInfo;
}
