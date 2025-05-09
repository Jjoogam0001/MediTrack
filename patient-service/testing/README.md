# MediTrack Patient Service Testing Resources

This directory contains resources for testing the MediTrack Patient Service API.

## Contents

1. **[Postman Test Guide](../postman_test_guide.md)**: A comprehensive guide for testing the API using Postman. It includes:
   - Server configuration details
   - All available endpoints with their HTTP methods, URLs, descriptions, request/response structures
   - Sample request and response data
   - Step-by-step instructions for testing with Postman

2. **[Sample Test Data](../sample_test_data.json)**: A collection of sample patient records that can be used for testing. This file contains 5 different patient records with diverse data to test various scenarios.

## How to Use These Resources

### Setting Up Postman

1. Download and install Postman from [https://www.postman.com/downloads/](https://www.postman.com/downloads/)
2. Create a new collection named "MediTrack Patient Service"
3. Follow the instructions in the Postman Test Guide to set up requests for each endpoint

### Using the Sample Test Data

1. For POST requests (creating a new patient), you can copy individual patient objects from the sample_test_data.json file
2. Make sure to remove any ID fields when creating new patients, as these will be generated by the server
3. For PUT requests (updating a patient), you'll need to include the ID of an existing patient in your request

### Testing Workflow

1. Start by testing the health check endpoint to verify the service is running
2. Create a patient using one of the sample records
3. Use the returned ID to test the GET, PUT, and DELETE endpoints
4. Repeat with different sample records to test various scenarios

## Server Configuration

- **Base URL**: `http://localhost:80`
- **Swagger UI**: `http://localhost:80/swagger-ui.html`
- **OpenAPI Docs**: `http://localhost:80/api-docs`

## Additional Notes

- The database is configured to use PostgreSQL running on localhost:5432 with database name "mydatabase"
- The application uses JPA with Hibernate as the ORM
- The server is configured to run on port 80
- Make sure the application is running before attempting to test the endpoints