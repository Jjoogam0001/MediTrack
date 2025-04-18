package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientCreateRequest;
import com.pm.patientservice.dto.PatientResponse;
import com.pm.patientservice.dto.PatientUpdateRequest;
import com.pm.patientservice.exception.PatientAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Implementation of the PatientService interface
 */
@Service
public class PatientServiceImpl implements PatientService {

    private static final Logger logger = LogManager.getLogger(PatientServiceImpl.class);

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final MeterRegistry meterRegistry;

    // Counters for tracking operations
    private final Counter patientCreatedCounter;
    private final Counter patientUpdatedCounter;
    private final Counter patientDeletedCounter;
    private final Counter patientRetrievedCounter;

    // Timers for measuring operation duration
    private final Timer createPatientTimer;
    private final Timer updatePatientTimer;
    private final Timer getPatientTimer;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper, MeterRegistry meterRegistry) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.patientCreatedCounter = Counter.builder("patient.created")
                .description("Number of patients created")
                .register(meterRegistry);
        this.patientUpdatedCounter = Counter.builder("patient.updated")
                .description("Number of patients updated")
                .register(meterRegistry);
        this.patientDeletedCounter = Counter.builder("patient.deleted")
                .description("Number of patients deleted")
                .register(meterRegistry);
        this.patientRetrievedCounter = Counter.builder("patient.retrieved")
                .description("Number of patient retrieval operations")
                .register(meterRegistry);

        // Initialize timers
        this.createPatientTimer = Timer.builder("patient.create.time")
                .description("Time taken to create a patient")
                .register(meterRegistry);
        this.updatePatientTimer = Timer.builder("patient.update.time")
                .description("Time taken to update a patient")
                .register(meterRegistry);
        this.getPatientTimer = Timer.builder("patient.get.time")
                .description("Time taken to retrieve a patient")
                .register(meterRegistry);
    }

    @Override
    @Transactional
    @Timed(value = "patient.create", description = "Time taken to create a patient")
    public PatientResponse createPatient(PatientCreateRequest request) {
        logger.info("Creating new patient with medical record number: {}", request.getMedicalRecordNumber());
        return createPatientTimer.record(() -> {
            try {
                // Check if patient with the same medical record number already exists
                if (patientRepository.existsByMedicalRecordNumber(request.getMedicalRecordNumber())) {
                    logger.warn("Patient with medical record number {} already exists", request.getMedicalRecordNumber());
                    throw new PatientAlreadyExistsException(request.getMedicalRecordNumber(), "medicalRecordNumber");
                }

                // Check if patient with the same email already exists
                if (patientRepository.existsByContactInfoEmail(request.getContactInfo().getEmail())) {
                    logger.warn("Patient with email {} already exists", request.getContactInfo().getEmail());
                    throw new PatientAlreadyExistsException(request.getContactInfo().getEmail(), "email");
                }

                // Check if patient with the same phone number already exists
                if (patientRepository.existsByContactInfoPhoneNumber(request.getContactInfo().getPhoneNumber())) {
                    logger.warn("Patient with phone number {} already exists", request.getContactInfo().getPhoneNumber());
                    throw new PatientAlreadyExistsException(request.getContactInfo().getPhoneNumber(), "phoneNumber");
                }

                // Convert request to entity
                logger.debug("Converting patient request to entity");
                Patient patient = patientMapper.toPatient(request);

                // Save the patient (ID will be automatically generated)
                logger.debug("Saving patient to database");
                Patient savedPatient = patientRepository.save(patient);

                // Increment the counter for patient creation
                patientCreatedCounter.increment();

                // Convert entity to response
                PatientResponse response = patientMapper.toPatientResponse(savedPatient);
                logger.info("Successfully created patient with ID: {}", response.getId());
                return response;
            } catch (Exception e) {
                if (!(e instanceof PatientAlreadyExistsException)) {
                    logger.error("Error creating patient: {}", e.getMessage(), e);
                }
                throw e;
            }
        });
    }

    @Override
    @Transactional(readOnly = true)
    @Timed(value = "patient.get.by.id", description = "Time taken to get a patient by ID")
    public PatientResponse getPatientById(String id) {
        logger.info("Retrieving patient with ID: {}", id);
        return getPatientTimer.record(() -> {
            try {
                Patient patient = patientRepository.findById(id)
                        .orElseThrow(() -> {
                            logger.warn("Patient with ID {} not found", id);
                            return new PatientNotFoundException(id, "id");
                        });

                patientRetrievedCounter.increment();
                PatientResponse response = patientMapper.toPatientResponse(patient);
                logger.debug("Successfully retrieved patient with ID: {}", id);
                return response;
            } catch (Exception e) {
                if (!(e instanceof PatientNotFoundException)) {
                    logger.error("Error retrieving patient with ID {}: {}", id, e.getMessage(), e);
                }
                throw e;
            }
        });
    }

    @Override
    @Transactional(readOnly = true)
    @Timed(value = "patient.get.by.mrn", description = "Time taken to get a patient by medical record number")
    public PatientResponse getPatientByMedicalRecordNumber(String medicalRecordNumber) {
        logger.info("Retrieving patient with medical record number: {}", medicalRecordNumber);
        return getPatientTimer.record(() -> {
            try {
                Patient patient = patientRepository.findByMedicalRecordNumber(medicalRecordNumber)
                        .orElseThrow(() -> {
                            logger.warn("Patient with medical record number {} not found", medicalRecordNumber);
                            return new PatientNotFoundException(medicalRecordNumber, "medicalRecordNumber");
                        });

                patientRetrievedCounter.increment();
                PatientResponse response = patientMapper.toPatientResponse(patient);
                logger.debug("Successfully retrieved patient with medical record number: {}", medicalRecordNumber);
                return response;
            } catch (Exception e) {
                if (!(e instanceof PatientNotFoundException)) {
                    logger.error("Error retrieving patient with medical record number {}: {}", medicalRecordNumber, e.getMessage(), e);
                }
                throw e;
            }
        });
    }

    @Override
    @Transactional(readOnly = true)
    @Timed(value = "patient.get.all", description = "Time taken to get all patients")
    public List<PatientResponse> getAllPatients() {
        logger.info("Retrieving all patients");
        try {
            patientRetrievedCounter.increment();
            List<PatientResponse> patients = patientRepository.findAll().stream()
                    .map(patientMapper::toPatientResponse)
                    .collect(Collectors.toList());
            logger.info("Successfully retrieved {} patients", patients.size());
            return patients;
        } catch (Exception e) {
            logger.error("Error retrieving all patients: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    @Timed(value = "patient.update", description = "Time taken to update a patient")
    public PatientResponse updatePatient(PatientUpdateRequest request) {
        logger.info("Updating patient with ID: {}", request.getId());
        return updatePatientTimer.record(() -> {
            try {
                // Check if patient exists
                Patient patient = patientRepository.findById(request.getId())
                        .orElseThrow(() -> {
                            logger.warn("Patient with ID {} not found for update", request.getId());
                            return new PatientNotFoundException(request.getId(), "id");
                        });

                // Check if the new medical record number is already used by another patient
                if (!patient.getMedicalRecordNumber().equals(request.getMedicalRecordNumber()) &&
                        patientRepository.existsByMedicalRecordNumber(request.getMedicalRecordNumber())) {
                    logger.warn("Medical record number {} is already in use by another patient", request.getMedicalRecordNumber());
                    throw new PatientAlreadyExistsException(request.getMedicalRecordNumber(), "medicalRecordNumber");
                }

                // Check if the new email is already used by another patient
                if (!patient.getContactInfo().getEmail().equals(request.getContactInfo().getEmail()) &&
                        patientRepository.existsByContactInfoEmail(request.getContactInfo().getEmail())) {
                    logger.warn("Email {} is already in use by another patient", request.getContactInfo().getEmail());
                    throw new PatientAlreadyExistsException(request.getContactInfo().getEmail(), "email");
                }

                // Check if the new phone number is already used by another patient
                if (!patient.getContactInfo().getPhoneNumber().equals(request.getContactInfo().getPhoneNumber()) &&
                        patientRepository.existsByContactInfoPhoneNumber(request.getContactInfo().getPhoneNumber())) {
                    logger.warn("Phone number {} is already in use by another patient", request.getContactInfo().getPhoneNumber());
                    throw new PatientAlreadyExistsException(request.getContactInfo().getPhoneNumber(), "phoneNumber");
                }

                // Update patient from request
                logger.debug("Updating patient data from request");
                patientMapper.updatePatientFromDTO(request, patient);

                // Save the updated patient
                logger.debug("Saving updated patient to database");
                Patient updatedPatient = patientRepository.save(patient);

                // Increment the counter for patient updates
                patientUpdatedCounter.increment();

                // Convert entity to response
                PatientResponse response = patientMapper.toPatientResponse(updatedPatient);
                logger.info("Successfully updated patient with ID: {}", response.getId());
                return response;
            } catch (Exception e) {
                if (!(e instanceof PatientNotFoundException) && !(e instanceof PatientAlreadyExistsException)) {
                    logger.error("Error updating patient with ID {}: {}", request.getId(), e.getMessage(), e);
                }
                throw e;
            }
        });
    }

    @Override
    @Transactional
    @Timed(value = "patient.delete", description = "Time taken to delete a patient")
    public void deletePatient(String id) {
        logger.info("Deleting patient with ID: {}", id);
        try {
            // Check if patient exists
            if (!patientRepository.existsById(id)) {
                logger.warn("Patient with ID {} not found for deletion", id);
                throw new PatientNotFoundException(id, "id");
            }

            logger.debug("Deleting patient from database");
            patientRepository.deleteById(id);

            // Increment the counter for patient deletions
            patientDeletedCounter.increment();
            logger.info("Successfully deleted patient with ID: {}", id);
        } catch (Exception e) {
            if (!(e instanceof PatientNotFoundException)) {
                logger.error("Error deleting patient with ID {}: {}", id, e.getMessage(), e);
            }
            throw e;
        }
    }
}
