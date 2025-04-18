package com.pm.patientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

/**
 * DTO for Insurance Information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceInfoDTO {
    @NotBlank(message = "Provider is required")
    @Size(max = 100, message = "Provider must be less than 100 characters")
    private String provider;

    @NotBlank(message = "Policy number is required")
    @Size(max = 50, message = "Policy number must be less than 50 characters")
    private String policyNumber;

    @Size(max = 50, message = "Group number must be less than 50 characters")
    private String groupNumber;

    @NotBlank(message = "Policy holder name is required")
    @Size(max = 100, message = "Policy holder name must be less than 100 characters")
    private String policyHolderName;

    @NotNull(message = "Effective date is required")
    private Date effectiveDate;

    @Future(message = "Expiration date must be in the future")
    private Date expirationDate;

    @NotBlank(message = "Coverage type is required")
    @Size(max = 50, message = "Coverage type must be less than 50 characters")
    private String coverageType;
}
