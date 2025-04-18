package com.pm.patientservice.controller;

import com.pm.patientservice.dto.HealthCheckResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;

/**
 * REST controller for health check operations
 */
@RestController
@RequestMapping("/api/health")
@Tag(name = "Health", description = "Health check API")
public class HealthCheckController {

    private static final Logger logger = LogManager.getLogger(HealthCheckController.class);

    private final JdbcTemplate jdbcTemplate;
    private final Environment environment;

    @Value("${spring.application.name:patient-service}")
    private String applicationName;

    @Value("${spring.profiles.active:development}")
    private String activeProfile;

    @Value("${app.version:0.0.1-SNAPSHOT}")
    private String appVersion;

    @Autowired
    public HealthCheckController(JdbcTemplate jdbcTemplate, Environment environment) {
        this.jdbcTemplate = jdbcTemplate;
        this.environment = environment;
    }

    /**
     * Get health check information
     *
     * @return health check information including service version, database status, and environment
     */
    @Operation(
        summary = "Get health check information",
        description = "Returns the service version, database status, and environment information"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Health check successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = HealthCheckResponse.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<HealthCheckResponse> getHealthCheck() {
        logger.info("Health check requested");

        String dbStatus = checkDatabaseStatus();
        String env = determineEnvironment();

        HealthCheckResponse response = new HealthCheckResponse(
            appVersion,
            env,
            dbStatus,
            new Date()
        );

        logger.info("Health check completed. Status: DB={}, Environment={}", dbStatus, env);
        return ResponseEntity.ok(response);
    }

    private String checkDatabaseStatus() {
        logger.debug("Checking database status");
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            logger.debug("Database check successful");
            return "UP";
        } catch (Exception e) {
            logger.error("Database check failed: {}", e.getMessage(), e);
            return "DOWN: " + e.getMessage();
        }
    }

    private String determineEnvironment() {
        logger.debug("Determining environment");
        // First check if spring.profiles.active is set
        if (activeProfile != null && !activeProfile.equals("development")) {
            logger.debug("Using active profile: {}", activeProfile);
            return activeProfile;
        }

        // Then check if any environment-specific profiles are active
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length > 0) {
            String profiles = String.join(",", activeProfiles);
            logger.debug("Using active profiles: {}", profiles);
            return profiles;
        }

        // Default to development
        logger.debug("No active profiles found, using default: development");
        return "development";
    }
}
