package com.pm.patientservice.controller;

import com.pm.patientservice.dto.PatientCreateRequest;
import com.pm.patientservice.dto.PatientResponse;
import com.pm.patientservice.dto.PatientUpdateRequest;
import com.pm.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for patient operations
 */
@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patient", description = "Patient management API")
public class PatientController {

    private static final Logger logger = LogManager.getLogger(PatientController.class);

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Create a new patient
     * 
     * @param request the patient creation request
     * @return the created patient
     */
    @Operation(
        summary = "Create a new patient",
        description = "Creates a new patient record with the provided information"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Patient created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PatientResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input data",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "Patient already exists",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(
            @Parameter(description = "Patient information for creating a new record", required = true)
            @Valid @RequestBody PatientCreateRequest request) {
        logger.info("Creating new patient with medical record number: {}", request.getMedicalRecordNumber());
        try {
            PatientResponse response = patientService.createPatient(request);
            logger.info("Successfully created patient with ID: {}", response.getId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Failed to create patient: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Get a patient by ID
     * 
     * @param id the patient ID
     * @return the patient
     */
    @Operation(
        summary = "Get a patient by ID",
        description = "Retrieves a patient record by their unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Patient found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PatientResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Patient not found",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatientById(
            @Parameter(description = "Unique identifier of the patient", required = true)
            @PathVariable String id) {
        logger.info("Retrieving patient with ID: {}", id);
        try {
            PatientResponse response = patientService.getPatientById(id);
            logger.debug("Successfully retrieved patient: {}", response.getMedicalRecordNumber());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to retrieve patient with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Get a patient by medical record number
     * 
     * @param medicalRecordNumber the medical record number
     * @return the patient
     */
    @Operation(
        summary = "Get a patient by medical record number",
        description = "Retrieves a patient record by their medical record number (MRN)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Patient found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PatientResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Patient not found",
            content = @Content
        )
    })
    @GetMapping("/mrn/{mrn}")
    public ResponseEntity<PatientResponse> getPatientByMedicalRecordNumber(
            @Parameter(description = "Medical record number of the patient", required = true)
            @PathVariable("mrn") String medicalRecordNumber) {
        logger.info("Retrieving patient with medical record number: {}", medicalRecordNumber);
        try {
            PatientResponse response = patientService.getPatientByMedicalRecordNumber(medicalRecordNumber);
            logger.debug("Successfully retrieved patient with medical record number: {}", medicalRecordNumber);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to retrieve patient with medical record number {}: {}", medicalRecordNumber, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Get all patients
     * 
     * @return list of all patients
     */
    @Operation(
        summary = "Get all patients",
        description = "Retrieves a list of all patient records in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "List of patients retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PatientResponse.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        logger.info("Retrieving all patients");
        try {
            List<PatientResponse> patients = patientService.getAllPatients();
            logger.info("Successfully retrieved {} patients", patients.size());
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            logger.error("Failed to retrieve all patients: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Update a patient
     * 
     * @param id the patient ID
     * @param request the patient update request
     * @return the updated patient
     */
    @Operation(
        summary = "Update a patient",
        description = "Updates an existing patient record with the provided information"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Patient updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PatientResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input data or ID mismatch",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Patient not found",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(
            @Parameter(description = "Unique identifier of the patient to update", required = true)
            @PathVariable String id, 
            @Parameter(description = "Updated patient information", required = true)
            @Valid @RequestBody PatientUpdateRequest request) {

        logger.info("Updating patient with ID: {}", id);

        // Ensure the ID in the path matches the ID in the request
        if (!id.equals(request.getId())) {
            logger.warn("ID mismatch in update request: path ID={}, body ID={}", id, request.getId());
            return ResponseEntity.badRequest().build();
        }

        try {
            PatientResponse response = patientService.updatePatient(request);
            logger.info("Successfully updated patient with ID: {}", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to update patient with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Delete a patient
     * 
     * @param id the patient ID
     * @return no content
     */
    @Operation(
        summary = "Delete a patient",
        description = "Deletes a patient record by their unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Patient deleted successfully",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Patient not found",
            content = @Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(
            @Parameter(description = "Unique identifier of the patient to delete", required = true)
            @PathVariable String id) {
        logger.info("Deleting patient with ID: {}", id);
        try {
            patientService.deletePatient(id);
            logger.info("Successfully deleted patient with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Failed to delete patient with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}
