package com.pm.patientservice.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.patientservice.dto.*;
import com.pm.patientservice.exception.PatientAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.service.PatientService;
import com.pm.patientservice.controller.PatientController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PatientService patientService;

    private PatientController patientController;

    private PatientCreateRequest createRequest;
    private PatientUpdateRequest updateRequest;
    private PatientResponse patientResponse;
    private String patientId;

    @BeforeEach
    void setUp() {
        // Initialize test data
        patientId = "test-uuid";

        // Create request
        createRequest = createTestPatientCreateRequest();

        // Update request
        updateRequest = createTestPatientUpdateRequest();

        // Patient response
        patientResponse = createTestPatientResponse();

        // Set up controller and mockMvc
        patientController = new PatientController(patientService);
        mockMvc = MockMvcBuilders.standaloneSetup(patientController)
                .setControllerAdvice(new com.pm.patientservice.exception.GlobalExceptionHandler())
                .build();
    }

    @Test
    void testCreatePatient() throws Exception {
        // Arrange
        when(patientService.createPatient(any(PatientCreateRequest.class))).thenReturn(patientResponse);

        // Act & Assert
        mockMvc.perform(post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(patientId)))
                .andExpect(jsonPath("$.medicalRecordNumber", is(createRequest.getMedicalRecordNumber())))
                .andExpect(jsonPath("$.firstName", is(createRequest.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(createRequest.getLastName())));

        // Verify
        verify(patientService).createPatient(any(PatientCreateRequest.class));
    }

    @Test
    void testCreatePatient_AlreadyExists() throws Exception {
        // Arrange
        when(patientService.createPatient(any(PatientCreateRequest.class)))
                .thenThrow(new PatientAlreadyExistsException("MRN12345", "medicalRecordNumber"));

        // Act & Assert
        mockMvc.perform(post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode", is("DUPLICATE_MEDICAL_RECORD_NUMBER")));

        // Verify
        verify(patientService).createPatient(any(PatientCreateRequest.class));
    }

    @Test
    void testGetPatientById() throws Exception {
        // Arrange
        when(patientService.getPatientById(patientId)).thenReturn(patientResponse);

        // Act & Assert
        mockMvc.perform(get("/api/patients/{id}", patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(patientId)))
                .andExpect(jsonPath("$.medicalRecordNumber", is(patientResponse.getMedicalRecordNumber())))
                .andExpect(jsonPath("$.firstName", is(patientResponse.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(patientResponse.getLastName())));

        // Verify
        verify(patientService).getPatientById(patientId);
    }

    @Test
    void testGetPatientById_NotFound() throws Exception {
        // Arrange
        when(patientService.getPatientById(patientId))
                .thenThrow(new PatientNotFoundException(patientId, "id"));

        // Act & Assert
        mockMvc.perform(get("/api/patients/{id}", patientId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is("PATIENT_NOT_FOUND")));

        // Verify
        verify(patientService).getPatientById(patientId);
    }

    @Test
    void testGetPatientByMedicalRecordNumber() throws Exception {
        // Arrange
        String mrn = "MRN12345";
        when(patientService.getPatientByMedicalRecordNumber(mrn)).thenReturn(patientResponse);

        // Act & Assert
        mockMvc.perform(get("/api/patients/mrn/{mrn}", mrn))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(patientId)))
                .andExpect(jsonPath("$.medicalRecordNumber", is(patientResponse.getMedicalRecordNumber())))
                .andExpect(jsonPath("$.firstName", is(patientResponse.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(patientResponse.getLastName())));

        // Verify
        verify(patientService).getPatientByMedicalRecordNumber(mrn);
    }

    @Test
    void testGetPatientByMedicalRecordNumber_NotFound() throws Exception {
        // Arrange
        String mrn = "MRN12345";
        when(patientService.getPatientByMedicalRecordNumber(mrn))
                .thenThrow(new PatientNotFoundException(mrn, "medicalRecordNumber"));

        // Act & Assert
        mockMvc.perform(get("/api/patients/mrn/{mrn}", mrn))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is("PATIENT_NOT_FOUND")));

        // Verify
        verify(patientService).getPatientByMedicalRecordNumber(mrn);
    }

    @Test
    void testGetAllPatients() throws Exception {
        // Arrange
        List<PatientResponse> patients = new ArrayList<>();
        patients.add(patientResponse);

        PatientResponse patient2 = new PatientResponse();
        patient2.setId("test-uuid-2");
        patient2.setMedicalRecordNumber("MRN12346");
        patient2.setFirstName("Jane");
        patient2.setLastName("Doe");
        patients.add(patient2);

        when(patientService.getAllPatients()).thenReturn(patients);

        // Act & Assert
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(patientId)))
                .andExpect(jsonPath("$[1].id", is("test-uuid-2")));

        // Verify
        verify(patientService).getAllPatients();
    }

    @Test
    void testUpdatePatient() throws Exception {
        // Arrange
        when(patientService.updatePatient(any(PatientUpdateRequest.class))).thenReturn(patientResponse);

        // Act & Assert
        mockMvc.perform(put("/api/patients/{id}", patientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(patientId)))
                .andExpect(jsonPath("$.medicalRecordNumber", is(patientResponse.getMedicalRecordNumber())))
                .andExpect(jsonPath("$.firstName", is(patientResponse.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(patientResponse.getLastName())));

        // Verify
        verify(patientService).updatePatient(any(PatientUpdateRequest.class));
    }

    @Test
    void testUpdatePatient_IdMismatch() throws Exception {
        // Arrange
        updateRequest.setId("different-id");

        // Act & Assert
        mockMvc.perform(put("/api/patients/{id}", patientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());

        // Verify
        verify(patientService, never()).updatePatient(any(PatientUpdateRequest.class));
    }

    @Test
    void testUpdatePatient_NotFound() throws Exception {
        // Arrange
        when(patientService.updatePatient(any(PatientUpdateRequest.class)))
                .thenThrow(new PatientNotFoundException(patientId, "id"));

        // Act & Assert
        mockMvc.perform(put("/api/patients/{id}", patientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is("PATIENT_NOT_FOUND")));

        // Verify
        verify(patientService).updatePatient(any(PatientUpdateRequest.class));
    }

    @Test
    void testDeletePatient() throws Exception {
        // Arrange
        doNothing().when(patientService).deletePatient(patientId);

        // Act & Assert
        mockMvc.perform(delete("/api/patients/{id}", patientId))
                .andExpect(status().isNoContent());

        // Verify
        verify(patientService).deletePatient(patientId);
    }

    @Test
    void testDeletePatient_NotFound() throws Exception {
        // Arrange
        doThrow(new PatientNotFoundException(patientId, "id"))
                .when(patientService).deletePatient(patientId);

        // Act & Assert
        mockMvc.perform(delete("/api/patients/{id}", patientId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is("PATIENT_NOT_FOUND")));

        // Verify
        verify(patientService).deletePatient(patientId);
    }

    // Helper methods to create test data

    private PatientCreateRequest createTestPatientCreateRequest() {
        PatientCreateRequest request = new PatientCreateRequest();
        request.setMedicalRecordNumber("MRN12345");
        request.setFirstName("John");
        request.setLastName("Doe");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -30);
        request.setDateOfBirth(calendar.getTime());

        request.setGender("Male");

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet("123 Main St");
        addressDTO.setCity("Anytown");
        addressDTO.setState("CA");
        addressDTO.setZipCode("12345");
        addressDTO.setCountry("USA");
        request.setAddress(addressDTO);

        ContactInfoDTO contactInfoDTO = new ContactInfoDTO();
        contactInfoDTO.setPhoneNumber("+1-555-123-4567");
        contactInfoDTO.setEmail("john.doe@example.com");
        request.setContactInfo(contactInfoDTO);

        return request;
    }

    private PatientUpdateRequest createTestPatientUpdateRequest() {
        PatientUpdateRequest request = new PatientUpdateRequest();
        request.setId(patientId);
        request.setMedicalRecordNumber("MRN12345");
        request.setFirstName("John");
        request.setLastName("Smith"); // Changed last name

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -30);
        request.setDateOfBirth(calendar.getTime());

        request.setGender("Male");

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet("456 New St"); // Changed street
        addressDTO.setCity("Anytown");
        addressDTO.setState("CA");
        addressDTO.setZipCode("12345");
        addressDTO.setCountry("USA");
        request.setAddress(addressDTO);

        ContactInfoDTO contactInfoDTO = new ContactInfoDTO();
        contactInfoDTO.setPhoneNumber("+1-555-123-4567");
        contactInfoDTO.setEmail("john.smith@example.com"); // Changed email
        request.setContactInfo(contactInfoDTO);

        return request;
    }

    private PatientResponse createTestPatientResponse() {
        PatientResponse response = new PatientResponse();
        response.setId(patientId);
        response.setMedicalRecordNumber("MRN12345");
        response.setFirstName("John");
        response.setLastName("Doe");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -30);
        response.setDateOfBirth(calendar.getTime());

        response.setGender("Male");

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet("123 Main St");
        addressDTO.setCity("Anytown");
        addressDTO.setState("CA");
        addressDTO.setZipCode("12345");
        addressDTO.setCountry("USA");
        response.setAddress(addressDTO);

        ContactInfoDTO contactInfoDTO = new ContactInfoDTO();
        contactInfoDTO.setPhoneNumber("+1-555-123-4567");
        contactInfoDTO.setEmail("john.doe@example.com");
        response.setContactInfo(contactInfoDTO);

        response.setCreatedAt(new Date());
        response.setUpdatedAt(new Date());

        return response;
    }
}