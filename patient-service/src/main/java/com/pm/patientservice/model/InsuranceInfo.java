package com.pm.patientservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * InsuranceInfo model representing insurance information for a patient.
 */
@Entity
@Table(name = "insurance_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider", nullable = false, length = 100)
    private String provider;

    @Column(name = "policy_number", nullable = false, length = 50)
    private String policyNumber;

    @Column(name = "group_number", length = 50)
    private String groupNumber;

    @Column(name = "policy_holder_name", nullable = false, length = 100)
    private String policyHolderName;

    @Temporal(TemporalType.DATE)
    @Column(name = "effective_date", nullable = false)
    private Date effectiveDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "coverage_type", nullable = false, length = 50)
    private String coverageType;

    // Constructor without id for easier creation
    public InsuranceInfo(String provider, String policyNumber, String groupNumber, 
                         String policyHolderName, Date effectiveDate, Date expirationDate, 
                         String coverageType) {
        this.provider = provider;
        this.policyNumber = policyNumber;
        this.groupNumber = groupNumber;
        this.policyHolderName = policyHolderName;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.coverageType = coverageType;
    }
}
