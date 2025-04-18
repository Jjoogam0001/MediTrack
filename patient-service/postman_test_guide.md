# MediTrack Patient Service API Testing Guide

This guide provides information on how to test the MediTrack Patient Service API using Postman.

## Server Configuration

- **Base URL**: `http://localhost:80`
- **Swagger UI**: `http://localhost:80/swagger-ui.html`
- **OpenAPI Docs**: `http://localhost:80/api-docs`

## Available Endpoints

### Health Check API

#### Get Health Check Information
- **Method**: GET
- **URL**: `http://localhost:80/api/health`
- **Description**: Returns the service version, database status, and environment information
- **Response**: HealthCheckResponse (200 OK)
  ```json
  {
    "serviceVersion": "0.0.1",
    "environment": "dev",
    "dbStatus": "UP",
    "timestamp": "2023-06-15T10:30:45.123Z"
  }
  ```

### Patient Management API

#### Create a New Patient
- **Method**: POST
- **URL**: `http://localhost:80/api/patients`
- **Description**: Creates a new patient record with the provided information
- **Request Body**: PatientCreateRequest
  ```json
  {
    "medicalRecordNumber": "MRN12345",
    "firstName": "John",
    "lastName": "Doe",
    "dateOfBirth": "1980-01-15",
    "gender": "Male",
    "address": {
      "street": "123 Main St",
      "city": "Boston",
      "state": "MA",
      "zipCode": "02108",
      "country": "USA"
    },
    "contactInfo": {
      "phoneNumber": "+1 555-123-4567",
      "email": "john.doe@example.com",
      "alternativePhoneNumber": "+1 555-987-6543"
    },
    "emergencyContacts": [
      {
        "name": "Jane Doe",
        "relationship": "Spouse",
        "phoneNumber": "+1 555-234-5678",
        "email": "jane.doe@example.com",
        "address": {
          "street": "123 Main St",
          "city": "Boston",
          "state": "MA",
          "zipCode": "02108",
          "country": "USA"
        }
      }
    ],
    "insuranceInfo": {
      "provider": "Blue Cross",
      "policyNumber": "BC123456789",
      "groupNumber": "GRP987654",
      "policyHolderName": "John Doe",
      "effectiveDate": "2023-01-01",
      "expirationDate": "2023-12-31",
      "coverageType": "Family"
    }
  }
  ```
- **Response**: PatientResponse (201 Created)
  ```json
  {
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "medicalRecordNumber": "MRN12345",
    "firstName": "John",
    "lastName": "Doe",
    "dateOfBirth": "1980-01-15",
    "gender": "Male",
    "address": {
      "street": "123 Main St",
      "city": "Boston",
      "state": "MA",
      "zipCode": "02108",
      "country": "USA"
    },
    "contactInfo": {
      "phoneNumber": "+1 555-123-4567",
      "email": "john.doe@example.com",
      "alternativePhoneNumber": "+1 555-987-6543"
    },
    "emergencyContacts": [
      {
        "name": "Jane Doe",
        "relationship": "Spouse",
        "phoneNumber": "+1 555-234-5678",
        "email": "jane.doe@example.com",
        "address": {
          "street": "123 Main St",
          "city": "Boston",
          "state": "MA",
          "zipCode": "02108",
          "country": "USA"
        }
      }
    ],
    "insuranceInfo": {
      "provider": "Blue Cross",
      "policyNumber": "BC123456789",
      "groupNumber": "GRP987654",
      "policyHolderName": "John Doe",
      "effectiveDate": "2023-01-01",
      "expirationDate": "2023-12-31",
      "coverageType": "Family"
    },
    "createdAt": "2023-06-15T10:30:45.123Z",
    "updatedAt": "2023-06-15T10:30:45.123Z"
  }
  ```

#### Get a Patient by ID
- **Method**: GET
- **URL**: `http://localhost:80/api/patients/{id}`
- **Description**: Retrieves a patient record by their unique identifier
- **Path Parameter**: id - Unique identifier of the patient
- **Response**: PatientResponse (200 OK)
  ```json
  {
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "medicalRecordNumber": "MRN12345",
    "firstName": "John",
    "lastName": "Doe",
    "dateOfBirth": "1980-01-15",
    "gender": "Male",
    "address": {
      "street": "123 Main St",
      "city": "Boston",
      "state": "MA",
      "zipCode": "02108",
      "country": "USA"
    },
    "contactInfo": {
      "phoneNumber": "+1 555-123-4567",
      "email": "john.doe@example.com",
      "alternativePhoneNumber": "+1 555-987-6543"
    },
    "emergencyContacts": [
      {
        "name": "Jane Doe",
        "relationship": "Spouse",
        "phoneNumber": "+1 555-234-5678",
        "email": "jane.doe@example.com",
        "address": {
          "street": "123 Main St",
          "city": "Boston",
          "state": "MA",
          "zipCode": "02108",
          "country": "USA"
        }
      }
    ],
    "insuranceInfo": {
      "provider": "Blue Cross",
      "policyNumber": "BC123456789",
      "groupNumber": "GRP987654",
      "policyHolderName": "John Doe",
      "effectiveDate": "2023-01-01",
      "expirationDate": "2023-12-31",
      "coverageType": "Family"
    },
    "createdAt": "2023-06-15T10:30:45.123Z",
    "updatedAt": "2023-06-15T10:30:45.123Z"
  }
  ```

#### Get a Patient by Medical Record Number
- **Method**: GET
- **URL**: `http://localhost:80/api/patients/mrn/{mrn}`
- **Description**: Retrieves a patient record by their medical record number (MRN)
- **Path Parameter**: mrn - Medical record number of the patient
- **Response**: PatientResponse (200 OK)
  ```json
  {
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "medicalRecordNumber": "MRN12345",
    "firstName": "John",
    "lastName": "Doe",
    "dateOfBirth": "1980-01-15",
    "gender": "Male",
    "address": {
      "street": "123 Main St",
      "city": "Boston",
      "state": "MA",
      "zipCode": "02108",
      "country": "USA"
    },
    "contactInfo": {
      "phoneNumber": "+1 555-123-4567",
      "email": "john.doe@example.com",
      "alternativePhoneNumber": "+1 555-987-6543"
    },
    "emergencyContacts": [
      {
        "name": "Jane Doe",
        "relationship": "Spouse",
        "phoneNumber": "+1 555-234-5678",
        "email": "jane.doe@example.com",
        "address": {
          "street": "123 Main St",
          "city": "Boston",
          "state": "MA",
          "zipCode": "02108",
          "country": "USA"
        }
      }
    ],
    "insuranceInfo": {
      "provider": "Blue Cross",
      "policyNumber": "BC123456789",
      "groupNumber": "GRP987654",
      "policyHolderName": "John Doe",
      "effectiveDate": "2023-01-01",
      "expirationDate": "2023-12-31",
      "coverageType": "Family"
    },
    "createdAt": "2023-06-15T10:30:45.123Z",
    "updatedAt": "2023-06-15T10:30:45.123Z"
  }
  ```

#### Get All Patients
- **Method**: GET
- **URL**: `http://localhost:80/api/patients`
- **Description**: Retrieves a list of all patient records in the system
- **Response**: Array of PatientResponse (200 OK)
  ```json
  [
    {
      "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "medicalRecordNumber": "MRN12345",
      "firstName": "John",
      "lastName": "Doe",
      "dateOfBirth": "1980-01-15",
      "gender": "Male",
      "address": {
        "street": "123 Main St",
        "city": "Boston",
        "state": "MA",
        "zipCode": "02108",
        "country": "USA"
      },
      "contactInfo": {
        "phoneNumber": "+1 555-123-4567",
        "email": "john.doe@example.com",
        "alternativePhoneNumber": "+1 555-987-6543"
      },
      "emergencyContacts": [
        {
          "name": "Jane Doe",
          "relationship": "Spouse",
          "phoneNumber": "+1 555-234-5678",
          "email": "jane.doe@example.com",
          "address": {
            "street": "123 Main St",
            "city": "Boston",
            "state": "MA",
            "zipCode": "02108",
            "country": "USA"
          }
        }
      ],
      "insuranceInfo": {
        "provider": "Blue Cross",
        "policyNumber": "BC123456789",
        "groupNumber": "GRP987654",
        "policyHolderName": "John Doe",
        "effectiveDate": "2023-01-01",
        "expirationDate": "2023-12-31",
        "coverageType": "Family"
      },
      "createdAt": "2023-06-15T10:30:45.123Z",
      "updatedAt": "2023-06-15T10:30:45.123Z"
    },
    {
      "id": "a1b2c3d4-e5f6-4a5b-9c3d-2e1f0a9b8c7d",
      "medicalRecordNumber": "MRN67890",
      "firstName": "Jane",
      "lastName": "Smith",
      "dateOfBirth": "1985-05-20",
      "gender": "Female",
      "address": {
        "street": "456 Oak Ave",
        "city": "New York",
        "state": "NY",
        "zipCode": "10001",
        "country": "USA"
      },
      "contactInfo": {
        "phoneNumber": "+1 555-345-6789",
        "email": "jane.smith@example.com",
        "alternativePhoneNumber": null
      },
      "emergencyContacts": [
        {
          "name": "John Smith",
          "relationship": "Spouse",
          "phoneNumber": "+1 555-456-7890",
          "email": "john.smith@example.com",
          "address": {
            "street": "456 Oak Ave",
            "city": "New York",
            "state": "NY",
            "zipCode": "10001",
            "country": "USA"
          }
        }
      ],
      "insuranceInfo": {
        "provider": "Aetna",
        "policyNumber": "AET987654321",
        "groupNumber": "GRP123456",
        "policyHolderName": "Jane Smith",
        "effectiveDate": "2023-01-01",
        "expirationDate": "2023-12-31",
        "coverageType": "Individual"
      },
      "createdAt": "2023-06-14T09:15:30.456Z",
      "updatedAt": "2023-06-14T09:15:30.456Z"
    }
  ]
  ```

#### Update a Patient
- **Method**: PUT
- **URL**: `http://localhost:80/api/patients/{id}`
- **Description**: Updates an existing patient record with the provided information
- **Path Parameter**: id - Unique identifier of the patient to update
- **Request Body**: PatientUpdateRequest
  ```json
  {
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "medicalRecordNumber": "MRN12345",
    "firstName": "John",
    "lastName": "Doe",
    "dateOfBirth": "1980-01-15",
    "gender": "Male",
    "address": {
      "street": "456 New St",
      "city": "Cambridge",
      "state": "MA",
      "zipCode": "02142",
      "country": "USA"
    },
    "contactInfo": {
      "phoneNumber": "+1 555-123-4567",
      "email": "john.doe.updated@example.com",
      "alternativePhoneNumber": "+1 555-987-6543"
    },
    "emergencyContacts": [
      {
        "name": "Jane Doe",
        "relationship": "Spouse",
        "phoneNumber": "+1 555-234-5678",
        "email": "jane.doe@example.com",
        "address": {
          "street": "456 New St",
          "city": "Cambridge",
          "state": "MA",
          "zipCode": "02142",
          "country": "USA"
        }
      }
    ],
    "insuranceInfo": {
      "provider": "Blue Cross",
      "policyNumber": "BC123456789",
      "groupNumber": "GRP987654",
      "policyHolderName": "John Doe",
      "effectiveDate": "2023-01-01",
      "expirationDate": "2023-12-31",
      "coverageType": "Family"
    }
  }
  ```
- **Response**: PatientResponse (200 OK)
  ```json
  {
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "medicalRecordNumber": "MRN12345",
    "firstName": "John",
    "lastName": "Doe",
    "dateOfBirth": "1980-01-15",
    "gender": "Male",
    "address": {
      "street": "456 New St",
      "city": "Cambridge",
      "state": "MA",
      "zipCode": "02142",
      "country": "USA"
    },
    "contactInfo": {
      "phoneNumber": "+1 555-123-4567",
      "email": "john.doe.updated@example.com",
      "alternativePhoneNumber": "+1 555-987-6543"
    },
    "emergencyContacts": [
      {
        "name": "Jane Doe",
        "relationship": "Spouse",
        "phoneNumber": "+1 555-234-5678",
        "email": "jane.doe@example.com",
        "address": {
          "street": "456 New St",
          "city": "Cambridge",
          "state": "MA",
          "zipCode": "02142",
          "country": "USA"
        }
      }
    ],
    "insuranceInfo": {
      "provider": "Blue Cross",
      "policyNumber": "BC123456789",
      "groupNumber": "GRP987654",
      "policyHolderName": "John Doe",
      "effectiveDate": "2023-01-01",
      "expirationDate": "2023-12-31",
      "coverageType": "Family"
    },
    "createdAt": "2023-06-15T10:30:45.123Z",
    "updatedAt": "2023-06-15T11:45:30.789Z"
  }
  ```

#### Delete a Patient
- **Method**: DELETE
- **URL**: `http://localhost:80/api/patients/{id}`
- **Description**: Deletes a patient record by their unique identifier
- **Path Parameter**: id - Unique identifier of the patient to delete
- **Response**: No Content (204)

## Testing with Postman

1. **Install Postman**: Download and install Postman from [https://www.postman.com/downloads/](https://www.postman.com/downloads/)

2. **Create a New Collection**:
   - Open Postman
   - Click on "Collections" in the sidebar
   - Click the "+" button to create a new collection
   - Name it "MediTrack Patient Service"

3. **Add Requests**:
   - For each endpoint described above, create a new request in the collection
   - Set the appropriate HTTP method (GET, POST, PUT, DELETE)
   - Set the URL as specified
   - For POST and PUT requests, add the sample JSON to the request body (select "raw" and "JSON" format)
   - For requests with path parameters, replace the placeholders with actual values after creating a patient

4. **Test the Endpoints**:
   - Start with the health check endpoint to verify the service is running
   - Create a new patient using the POST endpoint
   - Copy the returned ID to use in subsequent requests
   - Test the GET endpoints to retrieve the created patient
   - Test the PUT endpoint to update the patient
   - Test the DELETE endpoint to delete the patient

5. **Verify Responses**:
   - Check that the response status codes match the expected values
   - Verify that the response bodies contain the expected data

## Additional Notes

- The database is configured to use PostgreSQL running on localhost:5432 with database name "mydatabase"
- The application uses JPA with Hibernate as the ORM
- The server is configured to run on port 80
- Swagger UI is available at http://localhost:80/swagger-ui.html for interactive API documentation
- OpenAPI documentation is available at http://localhost:80/api-docs