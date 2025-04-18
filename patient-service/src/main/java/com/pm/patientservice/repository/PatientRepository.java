package com.pm.patientservice.repository;

import com.pm.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Patient entity
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {

    /**
     * Find a patient by medical record number
     * 
     * @param medicalRecordNumber the medical record number
     * @return the patient if found
     */
    Optional<Patient> findByMedicalRecordNumber(String medicalRecordNumber);

    /**
     * Check if a patient exists by medical record number
     * 
     * @param medicalRecordNumber the medical record number
     * @return true if exists, false otherwise
     */
    boolean existsByMedicalRecordNumber(String medicalRecordNumber);

    /**
     * Check if a patient exists by email
     * 
     * @param email the email
     * @return true if exists, false otherwise
     */
    boolean existsByContactInfoEmail(String email);

    /**
     * Check if a patient exists by phone number
     * 
     * @param phoneNumber the phone number
     * @return true if exists, false otherwise
     */
    boolean existsByContactInfoPhoneNumber(String phoneNumber);
}
