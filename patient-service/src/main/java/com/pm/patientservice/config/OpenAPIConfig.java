package com.pm.patientservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for OpenAPI documentation.
 */
@Configuration
public class OpenAPIConfig {

    @Value("${spring.application.name:Patient Service}")
    private String applicationName;

    /**
     * Configures the OpenAPI documentation for the application.
     *
     * @return the OpenAPI configuration
     */
    @Bean
    public OpenAPI openAPI() {
        Server localServer = new Server()
                .url("http://localhost")
                .description("Local Development Server");

        Contact contact = new Contact()
                .name("MediTrack Support")
                .email("support@meditrack.com")
                .url("https://meditrack.com/support");

        License license = new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0");

        Info info = new Info()
                .title(applicationName + " API")
                .version("1.0.0")
                .description("API documentation for the Patient Service of MediTrack application")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}
