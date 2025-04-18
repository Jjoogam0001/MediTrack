/**
 * Enum defining all possible error codes for the Patient Service
 */
package com.pm.patientservice.exception;

public enum ErrorCode {
    // General errors
    UNKNOWN_ERROR("UNKNOWN_ERROR", "An unknown error occurred"),
    VALIDATION_ERROR("VALIDATION_ERROR", "Validation error occurred"),
    
    // Patient-related errors
    PATIENT_NOT_FOUND("PATIENT_NOT_FOUND", "Patient not found"),
    PATIENT_ALREADY_EXISTS("PATIENT_ALREADY_EXISTS", "Patient already exists"),
    DUPLICATE_MEDICAL_RECORD_NUMBER("DUPLICATE_MEDICAL_RECORD_NUMBER", "Medical record number already exists"),
    DUPLICATE_EMAIL("DUPLICATE_EMAIL", "Email already exists"),
    DUPLICATE_PHONE_NUMBER("DUPLICATE_PHONE_NUMBER", "Phone number already exists"),
    
    // System errors
    DATABASE_ERROR("DATABASE_ERROR", "Database error occurred"),
    NETWORK_ERROR("NETWORK_ERROR", "Network error occurred");
    
    private final String code;
    private final String description;
    
    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}