package com.pm.patientservice.unit.service;

import com.pm.patientservice.dto.PatientCreateRequest;
import com.pm.patientservice.dto.PatientResponse;
import com.pm.patientservice.dto.PatientUpdateRequest;
import com.pm.patientservice.dto.AddressDTO;
import com.pm.patientservice.dto.ContactInfoDTO;
import com.pm.patientservice.dto.EmergencyContactDTO;
import com.pm.patientservice.dto.InsuranceInfoDTO;
import com.pm.patientservice.exception.PatientAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.model.Address;
import com.pm.patientservice.model.ContactInfo;
import com.pm.patientservice.repository.PatientRepository;
import com.pm.patientservice.service.PatientServiceImpl;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    private MeterRegistry meterRegistry;
    private PatientServiceImpl patientService;

    private PatientCreateRequest createRequest;
    private PatientUpdateRequest updateRequest;
    private Patient patient;
    private PatientResponse patientResponse;
    private String patientId;

    @BeforeEach
    void setUp() {
        // Initialize MeterRegistry and PatientServiceImpl
        meterRegistry = new SimpleMeterRegistry();
        patientService = new PatientServiceImpl(patientRepository, patientMapper, meterRegistry);

        // Initialize test data
        patientId = "test-uuid";

        // Create request
        createRequest = new PatientCreateRequest();
        createRequest.setMedicalRecordNumber("MRN12345");
        createRequest.setFirstName("John");
        createRequest.setLastName("Doe");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -30);
        createRequest.setDateOfBirth(calendar.getTime());

        createRequest.setGender("Male");

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet("123 Main St");
        addressDTO.setCity("Anytown");
        addressDTO.setState("CA");
        addressDTO.setZipCode("12345");
        addressDTO.setCountry("USA");
        createRequest.setAddress(addressDTO);

        ContactInfoDTO contactInfoDTO = new ContactInfoDTO();
        contactInfoDTO.setPhoneNumber("+1-555-123-4567");
        contactInfoDTO.setEmail("john.doe@example.com");
        createRequest.setContactInfo(contactInfoDTO);

        // Update request
        updateRequest = new PatientUpdateRequest();
        updateRequest.setId(patientId);
        updateRequest.setMedicalRecordNumber("MRN12345");
        updateRequest.setFirstName("John");
        updateRequest.setLastName("Smith"); // Changed last name
        updateRequest.setDateOfBirth(calendar.getTime());
        updateRequest.setGender("Male");
        updateRequest.setAddress(addressDTO);
        updateRequest.setContactInfo(contactInfoDTO);

        // Patient entity
        patient = new Patient();
        patient.setId(patientId);
        patient.setMedicalRecordNumber("MRN12345");
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth(calendar.getTime());
        patient.setGender("Male");

        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("Anytown");
        address.setState("CA");
        address.setZipCode("12345");
        address.setCountry("USA");
        patient.setAddress(address);

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setPhoneNumber("+1-555-123-4567");
        contactInfo.setEmail("john.doe@example.com");
        patient.setContactInfo(contactInfo);

        patient.setCreatedAt(new Date());
        patient.setUpdatedAt(new Date());

        // Patient response
        patientResponse = new PatientResponse();
        patientResponse.setId(patientId);
        patientResponse.setMedicalRecordNumber("MRN12345");
        patientResponse.setFirstName("John");
        patientResponse.setLastName("Doe");
        patientResponse.setDateOfBirth(calendar.getTime());
        patientResponse.setGender("Male");
        patientResponse.setAddress(addressDTO);
        patientResponse.setContactInfo(contactInfoDTO);
        patientResponse.setCreatedAt(new Date());
        patientResponse.setUpdatedAt(new Date());
    }

    @Test
    void testCreatePatient() {
        // Arrange
        when(patientRepository.existsByMedicalRecordNumber(anyString())).thenReturn(false);
        when(patientRepository.existsByContactInfoEmail(anyString())).thenReturn(false);
        when(patientRepository.existsByContactInfoPhoneNumber(anyString())).thenReturn(false);
        when(patientMapper.toPatient(any(PatientCreateRequest.class))).thenReturn(patient);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        when(patientMapper.toPatientResponse(any(Patient.class))).thenReturn(patientResponse);

        // Act
        PatientResponse result = patientService.createPatient(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals(patientId, result.getId());
        assertEquals("MRN12345", result.getMedicalRecordNumber());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        // Verify interactions
        verify(patientRepository).existsByMedicalRecordNumber("MRN12345");
        verify(patientRepository).existsByContactInfoEmail("john.doe@example.com");
        verify(patientRepository).existsByContactInfoPhoneNumber("+1-555-123-4567");
        verify(patientMapper).toPatient(createRequest);
        verify(patientRepository).save(patient);
        verify(patientMapper).toPatientResponse(patient);
    }

    @Test
    void testCreatePatient_DuplicateMedicalRecordNumber() {
        // Arrange
        when(patientRepository.existsByMedicalRecordNumber(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(PatientAlreadyExistsException.class, () -> {
            patientService.createPatient(createRequest);
        });

        // Verify interactions
        verify(patientRepository).existsByMedicalRecordNumber("MRN12345");
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void testCreatePatient_DuplicateEmail() {
        // Arrange
        when(patientRepository.existsByMedicalRecordNumber(anyString())).thenReturn(false);
        when(patientRepository.existsByContactInfoEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(PatientAlreadyExistsException.class, () -> {
            patientService.createPatient(createRequest);
        });

        // Verify interactions
        verify(patientRepository).existsByMedicalRecordNumber("MRN12345");
        verify(patientRepository).existsByContactInfoEmail("john.doe@example.com");
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void testCreatePatient_DuplicatePhoneNumber() {
        // Arrange
        when(patientRepository.existsByMedicalRecordNumber(anyString())).thenReturn(false);
        when(patientRepository.existsByContactInfoEmail(anyString())).thenReturn(false);
        when(patientRepository.existsByContactInfoPhoneNumber(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(PatientAlreadyExistsException.class, () -> {
            patientService.createPatient(createRequest);
        });

        // Verify interactions
        verify(patientRepository).existsByMedicalRecordNumber("MRN12345");
        verify(patientRepository).existsByContactInfoEmail("john.doe@example.com");
        verify(patientRepository).existsByContactInfoPhoneNumber("+1-555-123-4567");
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void testGetPatientById() {
        // Arrange
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(patientMapper.toPatientResponse(patient)).thenReturn(patientResponse);

        // Act
        PatientResponse result = patientService.getPatientById(patientId);

        // Assert
        assertNotNull(result);
        assertEquals(patientId, result.getId());

        // Verify interactions
        verify(patientRepository).findById(patientId);
        verify(patientMapper).toPatientResponse(patient);
    }

    @Test
    void testGetPatientById_NotFound() {
        // Arrange
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PatientNotFoundException.class, () -> {
            patientService.getPatientById(patientId);
        });

        // Verify interactions
        verify(patientRepository).findById(patientId);
        verify(patientMapper, never()).toPatientResponse(any(Patient.class));
    }

    @Test
    void testGetPatientByMedicalRecordNumber() {
        // Arrange
        String mrn = "MRN12345";
        when(patientRepository.findByMedicalRecordNumber(mrn)).thenReturn(Optional.of(patient));
        when(patientMapper.toPatientResponse(patient)).thenReturn(patientResponse);

        // Act
        PatientResponse result = patientService.getPatientByMedicalRecordNumber(mrn);

        // Assert
        assertNotNull(result);
        assertEquals(patientId, result.getId());
        assertEquals(mrn, result.getMedicalRecordNumber());

        // Verify interactions
        verify(patientRepository).findByMedicalRecordNumber(mrn);
        verify(patientMapper).toPatientResponse(patient);
    }

    @Test
    void testGetPatientByMedicalRecordNumber_NotFound() {
        // Arrange
        String mrn = "MRN12345";
        when(patientRepository.findByMedicalRecordNumber(mrn)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PatientNotFoundException.class, () -> {
            patientService.getPatientByMedicalRecordNumber(mrn);
        });

        // Verify interactions
        verify(patientRepository).findByMedicalRecordNumber(mrn);
        verify(patientMapper, never()).toPatientResponse(any(Patient.class));
    }

    @Test
    void testGetAllPatients() {
        // Arrange
        List<Patient> patients = new ArrayList<>();
        patients.add(patient);

        Patient patient2 = new Patient();
        patient2.setId("test-uuid-2");
        patient2.setMedicalRecordNumber("MRN12346");
        patients.add(patient2);

        when(patientRepository.findAll()).thenReturn(patients);
        when(patientMapper.toPatientResponse(any(Patient.class))).thenReturn(patientResponse);

        // Act
        List<PatientResponse> results = patientService.getAllPatients();

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());

        // Verify interactions
        verify(patientRepository).findAll();
        verify(patientMapper, times(2)).toPatientResponse(any(Patient.class));
    }

    @Test
    void testUpdatePatient() {
        // Arrange
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        when(patientMapper.toPatientResponse(any(Patient.class))).thenReturn(patientResponse);

        // Act
        PatientResponse result = patientService.updatePatient(updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(patientId, result.getId());

        // Verify interactions
        verify(patientRepository).findById(patientId);
        verify(patientMapper).updatePatientFromDTO(updateRequest, patient);
        verify(patientRepository).save(patient);
        verify(patientMapper).toPatientResponse(patient);
    }

    @Test
    void testUpdatePatient_NotFound() {
        // Arrange
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PatientNotFoundException.class, () -> {
            patientService.updatePatient(updateRequest);
        });

        // Verify interactions
        verify(patientRepository).findById(patientId);
        verify(patientMapper, never()).updatePatientFromDTO(any(), any());
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void testUpdatePatient_DuplicateMedicalRecordNumber() {
        // Arrange
        updateRequest.setMedicalRecordNumber("MRN12346"); // Different from original

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(patientRepository.existsByMedicalRecordNumber("MRN12346")).thenReturn(true);

        // Act & Assert
        assertThrows(PatientAlreadyExistsException.class, () -> {
            patientService.updatePatient(updateRequest);
        });

        // Verify interactions
        verify(patientRepository).findById(patientId);
        verify(patientRepository).existsByMedicalRecordNumber("MRN12346");
        verify(patientMapper, never()).updatePatientFromDTO(any(), any());
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void testDeletePatient() {
        // Arrange
        when(patientRepository.existsById(patientId)).thenReturn(true);

        // Act
        patientService.deletePatient(patientId);

        // Verify interactions
        verify(patientRepository).existsById(patientId);
        verify(patientRepository).deleteById(patientId);
    }

    @Test
    void testDeletePatient_NotFound() {
        // Arrange
        when(patientRepository.existsById(patientId)).thenReturn(false);

        // Act & Assert
        assertThrows(PatientNotFoundException.class, () -> {
            patientService.deletePatient(patientId);
        });

        // Verify interactions
        verify(patientRepository).existsById(patientId);
        verify(patientRepository, never()).deleteById(anyString());
    }
}
