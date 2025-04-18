package com.pm.patientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO for Health Check response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckResponse {
    private String serviceVersion;
    private String environment;
    private String dbStatus;
    private Date timestamp;
}