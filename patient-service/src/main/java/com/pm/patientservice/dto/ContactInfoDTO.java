package com.pm.patientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO for Contact Information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfoDTO {
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9\\s-()]{8,20}$", message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String email;

    @Pattern(regexp = "^$|^\\+?[0-9\\s-()]{8,20}$", message = "Invalid alternative phone number format")
    private String alternativePhoneNumber;
}
