package com.pm.patientservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ContactInfo model representing contact information for a patient.
 */
@Entity
@Table(name = "contact_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", nullable = false, length = 20, unique = true)
    private String phoneNumber;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "alternative_phone_number", length = 20)
    private String alternativePhoneNumber;

    // Constructor without id for easier creation
    public ContactInfo(String phoneNumber, String email, String alternativePhoneNumber) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.alternativePhoneNumber = alternativePhoneNumber;
    }
}
