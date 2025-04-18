package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientCreateRequest;
import com.pm.patientservice.dto.PatientResponse;
import com.pm.patientservice.dto.PatientUpdateRequest;

import java.util.List;

/**
 * Service interface for patient operations
 */
public interface PatientService {
    
    /**
     * Create a new patient
     * 
     * @param request the patient creation request
     * @return the created patient
     */
    PatientResponse createPatient(PatientCreateRequest request);
    
    /**
     * Get a patient by ID
     * 
     * @param id the patient ID
     * @return the patient
     */
    PatientResponse getPatientById(String id);
    
    /**
     * Get a patient by medical record number
     * 
     * @param medicalRecordNumber the medical record number
     * @return the patient
     */
    PatientResponse getPatientByMedicalRecordNumber(String medicalRecordNumber);
    
    /**
     * Get all patients
     * 
     * @return list of all patients
     */
    List<PatientResponse> getAllPatients();
    
    /**
     * Update a patient
     * 
     * @param request the patient update request
     * @return the updated patient
     */
    PatientResponse updatePatient(PatientUpdateRequest request);
    
    /**
     * Delete a patient by ID
     * 
     * @param id the patient ID
     */
    void deletePatient(String id);
}