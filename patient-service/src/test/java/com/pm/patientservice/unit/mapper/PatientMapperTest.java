package com.pm.patientservice.unit.mapper;

import com.pm.patientservice.dto.*;
import com.pm.patientservice.model.*;
import com.pm.patientservice.mapper.PatientMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PatientMapperTest {

    private PatientMapper patientMapper;
    private Patient patient;
    private PatientCreateRequest createRequest;
    private PatientUpdateRequest updateRequest;
    private Date testDate;
    private Date createdAt;
    private Date updatedAt;

    @BeforeEach
    void setUp() {
        patientMapper = new PatientMapper();

        // Create common test date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -30);
        testDate = calendar.getTime();

        createdAt = new Date();

        // Set updatedAt to be 1 hour after createdAt
        Calendar updatedCalendar = Calendar.getInstance();
        updatedCalendar.setTime(createdAt);
        updatedCalendar.add(Calendar.HOUR, 1);
        updatedAt = updatedCalendar.getTime();

        // Create test patient entity
        patient = createTestPatient();

        // Create test request DTOs
        createRequest = createTestPatientCreateRequest();
        updateRequest = createTestPatientUpdateRequest();
    }

    @Test
    void testToPatientResponse() {
        // Act
        PatientResponse response = patientMapper.toPatientResponse(patient);

        // Assert
        assertNotNull(response);
        assertEquals(patient.getId(), response.getId());
        assertEquals(patient.getMedicalRecordNumber(), response.getMedicalRecordNumber());
        assertEquals(patient.getFirstName(), response.getFirstName());
        assertEquals(patient.getLastName(), response.getLastName());
        assertEquals(patient.getDateOfBirth(), response.getDateOfBirth());
        assertEquals(patient.getGender(), response.getGender());
        assertEquals(patient.getCreatedAt(), response.getCreatedAt());
        assertEquals(patient.getUpdatedAt(), response.getUpdatedAt());

        // Check address mapping
        assertNotNull(response.getAddress());
        assertEquals(patient.getAddress().getStreet(), response.getAddress().getStreet());
        assertEquals(patient.getAddress().getCity(), response.getAddress().getCity());
        assertEquals(patient.getAddress().getState(), response.getAddress().getState());
        assertEquals(patient.getAddress().getZipCode(), response.getAddress().getZipCode());
        assertEquals(patient.getAddress().getCountry(), response.getAddress().getCountry());

        // Check contact info mapping
        assertNotNull(response.getContactInfo());
        assertEquals(patient.getContactInfo().getPhoneNumber(), response.getContactInfo().getPhoneNumber());
        assertEquals(patient.getContactInfo().getEmail(), response.getContactInfo().getEmail());
        assertEquals(patient.getContactInfo().getAlternativePhoneNumber(), response.getContactInfo().getAlternativePhoneNumber());

        // Check emergency contacts mapping
        assertNotNull(response.getEmergencyContacts());
        assertEquals(1, response.getEmergencyContacts().size());
        assertEquals(patient.getEmergencyContacts().get(0).getName(), response.getEmergencyContacts().get(0).getName());

        // Check insurance info mapping
        assertNotNull(response.getInsuranceInfo());
        assertEquals(patient.getInsuranceInfo().getProvider(), response.getInsuranceInfo().getProvider());
        assertEquals(patient.getInsuranceInfo().getPolicyNumber(), response.getInsuranceInfo().getPolicyNumber());
    }

    @Test
    void testToPatient() {
        // Act
        Patient result = patientMapper.toPatient(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals(createRequest.getMedicalRecordNumber(), result.getMedicalRecordNumber());
        assertEquals(createRequest.getFirstName(), result.getFirstName());
        assertEquals(createRequest.getLastName(), result.getLastName());
        assertEquals(createRequest.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(createRequest.getGender(), result.getGender());

        // Check timestamps
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        // Check address mapping
        assertNotNull(result.getAddress());
        assertEquals(createRequest.getAddress().getStreet(), result.getAddress().getStreet());
        assertEquals(createRequest.getAddress().getCity(), result.getAddress().getCity());
        assertEquals(createRequest.getAddress().getState(), result.getAddress().getState());
        assertEquals(createRequest.getAddress().getZipCode(), result.getAddress().getZipCode());
        assertEquals(createRequest.getAddress().getCountry(), result.getAddress().getCountry());

        // Check contact info mapping
        assertNotNull(result.getContactInfo());
        assertEquals(createRequest.getContactInfo().getPhoneNumber(), result.getContactInfo().getPhoneNumber());
        assertEquals(createRequest.getContactInfo().getEmail(), result.getContactInfo().getEmail());
        assertEquals(createRequest.getContactInfo().getAlternativePhoneNumber(), result.getContactInfo().getAlternativePhoneNumber());

        // Check emergency contacts mapping
        assertNotNull(result.getEmergencyContacts());
        assertEquals(1, result.getEmergencyContacts().size());
        assertEquals(createRequest.getEmergencyContacts().get(0).getName(), result.getEmergencyContacts().get(0).getName());

        // Check insurance info mapping
        assertNotNull(result.getInsuranceInfo());
        assertEquals(createRequest.getInsuranceInfo().getProvider(), result.getInsuranceInfo().getProvider());
        assertEquals(createRequest.getInsuranceInfo().getPolicyNumber(), result.getInsuranceInfo().getPolicyNumber());
    }

    @Test
    void testUpdatePatientFromDTO() {
        // Arrange
        Patient patientToUpdate = createTestPatient();
        patientToUpdate.setFirstName("Original");
        patientToUpdate.setLastName("Name");

        // Store the original updatedAt value
        Date originalUpdatedAt = patientToUpdate.getUpdatedAt();

        // Add a small delay to ensure the new Date() will be different
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // Ignore
        }

        // Act
        patientMapper.updatePatientFromDTO(updateRequest, patientToUpdate);

        // Assert
        assertEquals(updateRequest.getFirstName(), patientToUpdate.getFirstName());
        assertEquals(updateRequest.getLastName(), patientToUpdate.getLastName());
        assertEquals(updateRequest.getMedicalRecordNumber(), patientToUpdate.getMedicalRecordNumber());
        assertEquals(updateRequest.getDateOfBirth(), patientToUpdate.getDateOfBirth());
        assertEquals(updateRequest.getGender(), patientToUpdate.getGender());

        // Check address mapping
        assertNotNull(patientToUpdate.getAddress());
        assertEquals(updateRequest.getAddress().getStreet(), patientToUpdate.getAddress().getStreet());

        // Check contact info mapping
        assertNotNull(patientToUpdate.getContactInfo());
        assertEquals(updateRequest.getContactInfo().getEmail(), patientToUpdate.getContactInfo().getEmail());

        // Check update timestamp
        assertNotNull(patientToUpdate.getUpdatedAt());
        assertNotEquals(originalUpdatedAt, patientToUpdate.getUpdatedAt());
    }


    @Test
    void testNullInputs() {
        // Test null inputs for all mapper methods
        assertNull(patientMapper.toPatientResponse(null));
        assertNull(patientMapper.toPatient(null));

        // Test updatePatientFromDTO with null inputs
        Patient testPatient = new Patient();
        patientMapper.updatePatientFromDTO(null, testPatient);
        // No exception should be thrown, and patient should remain unchanged

        patientMapper.updatePatientFromDTO(updateRequest, null);
        // No exception should be thrown
    }

    // Helper methods to create test data

    private Patient createTestPatient() {
        Patient patient = new Patient();
        patient.setId("test-uuid");
        patient.setMedicalRecordNumber("MRN12345");
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth(testDate);
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
        contactInfo.setAlternativePhoneNumber("+1-555-987-6543");
        patient.setContactInfo(contactInfo);

        List<EmergencyContact> emergencyContacts = new ArrayList<>();
        EmergencyContact emergencyContact = new EmergencyContact();
        emergencyContact.setName("Jane Doe");
        emergencyContact.setRelationship("Spouse");
        emergencyContact.setPhoneNumber("+1-555-987-6543");
        emergencyContact.setEmail("jane.doe@example.com");
        emergencyContacts.add(emergencyContact);
        patient.setEmergencyContacts(emergencyContacts);

        InsuranceInfo insuranceInfo = new InsuranceInfo();
        insuranceInfo.setProvider("Health Insurance Co");
        insuranceInfo.setPolicyNumber("POL123456");
        insuranceInfo.setGroupNumber("GRP123456");
        insuranceInfo.setPolicyHolderName("John Doe");

        Calendar calendar = Calendar.getInstance();
        insuranceInfo.setEffectiveDate(calendar.getTime());

        calendar.add(Calendar.YEAR, 1);
        insuranceInfo.setExpirationDate(calendar.getTime());

        insuranceInfo.setCoverageType("Full Coverage");
        patient.setInsuranceInfo(insuranceInfo);

        patient.setCreatedAt(createdAt);
        patient.setUpdatedAt(updatedAt);

        return patient;
    }

    private PatientCreateRequest createTestPatientCreateRequest() {
        PatientCreateRequest request = new PatientCreateRequest();
        request.setMedicalRecordNumber("MRN12345");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setDateOfBirth(testDate);
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
        contactInfoDTO.setAlternativePhoneNumber("+1-555-987-6543");
        request.setContactInfo(contactInfoDTO);

        List<EmergencyContactDTO> emergencyContacts = new ArrayList<>();
        EmergencyContactDTO emergencyContact = new EmergencyContactDTO();
        emergencyContact.setName("Jane Doe");
        emergencyContact.setRelationship("Spouse");
        emergencyContact.setPhoneNumber("+1-555-987-6543");
        emergencyContact.setEmail("jane.doe@example.com");
        emergencyContacts.add(emergencyContact);
        request.setEmergencyContacts(emergencyContacts);

        InsuranceInfoDTO insuranceInfo = new InsuranceInfoDTO();
        insuranceInfo.setProvider("Health Insurance Co");
        insuranceInfo.setPolicyNumber("POL123456");
        insuranceInfo.setGroupNumber("GRP123456");
        insuranceInfo.setPolicyHolderName("John Doe");

        Calendar calendar = Calendar.getInstance();
        insuranceInfo.setEffectiveDate(calendar.getTime());

        calendar.add(Calendar.YEAR, 1);
        insuranceInfo.setExpirationDate(calendar.getTime());

        insuranceInfo.setCoverageType("Full Coverage");
        request.setInsuranceInfo(insuranceInfo);

        return request;
    }

    private PatientUpdateRequest createTestPatientUpdateRequest() {
        PatientUpdateRequest request = new PatientUpdateRequest();
        request.setId("test-uuid");
        request.setMedicalRecordNumber("MRN12345");
        request.setFirstName("John");
        request.setLastName("Smith"); // Changed last name
        request.setDateOfBirth(testDate);
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
        contactInfoDTO.setAlternativePhoneNumber("+1-555-987-6543");
        request.setContactInfo(contactInfoDTO);

        List<EmergencyContactDTO> emergencyContacts = new ArrayList<>();
        EmergencyContactDTO emergencyContact = new EmergencyContactDTO();
        emergencyContact.setName("Jane Smith"); // Changed name
        emergencyContact.setRelationship("Spouse");
        emergencyContact.setPhoneNumber("+1-555-987-6543");
        emergencyContact.setEmail("jane.smith@example.com"); // Changed email
        emergencyContacts.add(emergencyContact);
        request.setEmergencyContacts(emergencyContacts);

        InsuranceInfoDTO insuranceInfo = new InsuranceInfoDTO();
        insuranceInfo.setProvider("New Health Insurance Co"); // Changed provider
        insuranceInfo.setPolicyNumber("POL123456");
        insuranceInfo.setGroupNumber("GRP123456");
        insuranceInfo.setPolicyHolderName("John Smith"); // Changed name

        Calendar calendar = Calendar.getInstance();
        insuranceInfo.setEffectiveDate(calendar.getTime());

        calendar.add(Calendar.YEAR, 1);
        insuranceInfo.setExpirationDate(calendar.getTime());

        insuranceInfo.setCoverageType("Full Coverage");
        request.setInsuranceInfo(insuranceInfo);

        return request;
    }
}