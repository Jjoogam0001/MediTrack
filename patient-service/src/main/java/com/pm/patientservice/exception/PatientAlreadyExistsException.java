package com.pm.patientservice.exception;

/**
 * Exception thrown when attempting to create a patient that already exists
 */
public class PatientAlreadyExistsException extends PatientServiceException {

    public PatientAlreadyExistsException(String message) {
        super(message, ErrorCode.PATIENT_ALREADY_EXISTS);
    }

    public PatientAlreadyExistsException(String value, String field) {
        super(String.format("Patient with %s: %s already exists", field, value), 
              getErrorCodeForField(field));
    }

    /**
     * Get the appropriate error code based on the field that caused the exception
     */
    private static ErrorCode getErrorCodeForField(String field) {
        switch (field.toLowerCase()) {
            case "medicalrecordnumber":
                return ErrorCode.DUPLICATE_MEDICAL_RECORD_NUMBER;
            case "email":
                return ErrorCode.DUPLICATE_EMAIL;
            case "phonenumber":
                return ErrorCode.DUPLICATE_PHONE_NUMBER;
            default:
                return ErrorCode.PATIENT_ALREADY_EXISTS;
        }
    }
}
