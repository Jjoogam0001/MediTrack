package com.pm.patientservice.exception;

/**
 * Exception thrown when a patient is not found
 */
public class PatientNotFoundException extends PatientServiceException {

    public PatientNotFoundException(String message) {
        super(message, ErrorCode.PATIENT_NOT_FOUND);
    }

    public PatientNotFoundException(String id, String field) {
        super(String.format("Patient with %s: %s not found", field, id), ErrorCode.PATIENT_NOT_FOUND);
    }
}
